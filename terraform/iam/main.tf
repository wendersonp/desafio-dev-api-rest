resource "aws_iam_group" "ecr_group" {
  name = "ecr-group"
}

resource "aws_iam_group_policy_attachment" "policy_attachment" {
  for_each = toset(var.ecr_policy_arn)  

  group = aws_iam_group.ecr_group.name
  policy_arn = each.value
}

resource "aws_iam_user" "ecr_user" {
  name = "github-ecr-user"
}

resource "aws_iam_user_group_membership" "ecr_group_membership" {
  user = aws_iam_user.ecr_user.name
  groups = [aws_iam_group.ecr_group.name]
}

resource "aws_iam_access_key" "github_user_access_key" {
  user = aws_iam_user.ecr_user.name
}