# Infrastructure installation.
 * Create a aws account.
 * Create an IAM user with access to the API and with the `AdministratorAccess`.
 * Configure your credentials locally with the command `aws configure`.
 * Create a bucket with the command : `aws s3 mb s3://loose-touch-configuration --region eu-west-3` (Do it if it does not exists yet).
 * Add a life cycle rule cleaning the bucket files after 2 days.
 * Type `./deploy environement` where `environment` is `test`, `stage` or `production`.
 
_note : you can delete a whole stack with the command ̀`aws cloudformation delete-stack --stack-name STACK_NAME --region eu-west-3` (where STACK_NAME is `loose-touch-test`, `loose-touch-stage` or `loose-touch-production`)._

# DNS Configuration.
In google domains, set the nameserver of the aws hosted zone.