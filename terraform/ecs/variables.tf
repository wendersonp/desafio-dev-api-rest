variable "region" {
  default = "us-east-2"
  type = string
  description = "Regiao da AWS"
}

variable "rds_security_group_id" {
  type = string
  description = "rds_security_group_id"
}

variable "vpc_id" {
  type = string
  description = "Id da VPC criada"
}