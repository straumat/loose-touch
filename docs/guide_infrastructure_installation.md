# Infrastructure installation.
 * Create an aws account.
 * Create an IAM user with access to the API and with the AdministratorAccess.
 * Configure your credentials locally with the command `aws configure`.
 * Create a bucket with the command : `aws s3 mb s3://loose-touch-staging --region eu-west-3` (Do it if it does not exists yet).
 * Package the application with the command `mvn clean package`.
 * Package for aws : `aws cloudformation package --template-file aws_configuration_staging.yaml --output-template-file aws-sam-output-staging.yaml --s3-bucket loose-touch-staging --region eu-west-3`.
 * Then run this to deploy : `aws cloudformation deploy --template-file aws-sam-output-staging.yaml --stack-name loose-touch-staging --capabilities CAPABILITY_IAM --region eu-west-3`.
 * Once the application is deployed, you can get the details with the command `aws cloudformation describe-stacks --stack-name loose-touch-staging --region eu-west-3`.

_note : you can delete your whole stack with the command ̀`aws cloudformation delete-stack --stack-name loose-touch-staging --region eu-west-3`_