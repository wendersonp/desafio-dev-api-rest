data "aws_vpc" "digital_account_vpc" {
  id = var.vpc_id
}

data "aws_route_tables" "private_route_tables" {
  filter {
    name = "vpc-id"
    values = [var.vpc_id]
  }
  filter {
    name = "tag:Name"
    values = ["*private*"]
  }
}

data "aws_route_tables" "public_route_tables" {
  filter {
    name = "vpc-id"
    values = [var.vpc_id]
  }
  filter {
    name = "tag:Name"
    values = ["*public*"]
  }
}

data "aws_subnets" "public_subnets" {
  filter {
    name = "vpc-id"
    values = [var.vpc_id]
  }
  filter {
    name = "tag:Name"
    values = ["*public*"]
  }
}

data "aws_subnets" "private_subnets" {
  filter {
    name = "vpc-id"
    values = [var.vpc_id]
  }
  filter {
    name = "tag:Name"
    values = ["*private*"]
  }
}

resource "aws_eip" "aws_digital_account_eip" {
  domain = "vpc"
}

resource "aws_nat_gateway" "digital_account_nat_gateway" {
  allocation_id = aws_eip.aws_digital_account_eip.allocation_id
  subnet_id = data.aws_subnets.public_subnets.ids[0]
}

resource "aws_route" "nat_route" {
  for_each = toset(data.aws_route_tables.private_route_tables.ids)
  route_table_id = each.value
  destination_cidr_block = "0.0.0.0/0"
  nat_gateway_id = aws_nat_gateway.digital_account_nat_gateway.id
}

resource "aws_cloudwatch_log_group" "vpc_flowlogs" {
  name              = "/aws/vpc/flowlogs/${data.aws_vpc.digital_account_vpc.id}"
  retention_in_days = 14

  tags = {
    Name      = "vpc-flowlogs"
    ManagedBy = "terraform"
  }
}

resource "aws_iam_role" "vpc_flowlogs_role" {
  name = "vpc-flowlogs-role-${data.aws_vpc.digital_account_vpc.id}"

  assume_role_policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": {
          "Service": "vpc-flow-logs.amazonaws.com"
        },
        "Action": "sts:AssumeRole",
        "Sid": ""
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "vpc_flowlogs_attach" {
  role       = aws_iam_role.vpc_flowlogs_role.name
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchLogsFullAccess"
}

resource "aws_flow_log" "vpc_flowlogs" {

  vpc_id             = data.aws_vpc.digital_account_vpc.id
  traffic_type       = "ALL"
  log_destination    = aws_cloudwatch_log_group.vpc_flowlogs.arn
  log_destination_type = "cloud-watch-logs"
  iam_role_arn       = aws_iam_role.vpc_flowlogs_role.arn

  tags = {
    Name      = "vpc-flowlogs"
    ManagedBy = "terraform"
  }
}