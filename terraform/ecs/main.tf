data "aws_vpc" "digital_account_vpc" {
    id = var.vpc_id
}

data "aws_subnets" "private_subnets" {
    filter {
      name = "vpc-id"
      values = [var.vpc_id]
    }
}

resource "aws_ecs_cluster" "digital_account_cluster" {
    name = "digital-account-cluster"

    setting {
      name = "containerInsights"
      value = "enabled"
    }

    tags = {
      Environment = "staging"
      ManagedBy = "terraform"
    }
}

resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecs-task-execution-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    ManagedBy = "terraform"
  }
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_policy" {
    role = aws_iam_role.ecs_task_execution_role.name
    policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_security_group" "ecs_security_group" {
    name = "digital-account-services-sg"
    description = "Grupo de seguranca para os servicos da conta digital"
    vpc_id = data.aws_vpc.digital_account_vpc.id

    egress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags = {
      ManagedBy = "terraform"
    }

}




