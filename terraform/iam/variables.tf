variable "region" {
  default = "us-east-2"
  type = string
  description = "Regiao da AWS"
}

variable "ecr_policy_arn" {
  default = ["arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryPowerUser"]
  type = list(string)
}