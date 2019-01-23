# [Loose touch](http://www.loosetouch.com)
_Loose touch reminds you to keep in touch with your key contacts_

## Run the project.
The project folder includes a `aws_configuration_staging.yaml` file. 

You can use [AWS SAM Local](https://github.com/awslabs/aws-sam-local) to start this project in local : `sam local start-api --template aws_configuration_staging.yaml`

_note : you can validate your sam file locally with the command `sam validate -t aws_configuration_staging.yaml`._

You can use this [SAM](https://github.com/awslabs/serverless-application-model) file to deploy the project to AWS Lambda and Amazon API Gateway :
 * Package the application with the command `mvn clean package`.
 * Package for aws : `aws cloudformation package --template-file aws_configuration_staging.yaml --output-template-file aws-sam-output-staging.yaml --s3-bucket loose-touch-staging --region eu-west-3`.
 * Then run this to deploy : `aws cloudformation deploy --template-file aws-sam-output-staging.yaml --stack-name loose-touch-staging --capabilities CAPABILITY_IAM --region eu-west-3`.
 * Once the application is deployed, you can get the details with the command `aws cloudformation describe-stacks --stack-name loose-touch-staging --region eu-west-3`.

_note : you can delete your whole stack with the command ̀`aws cloudformation delete-stack --stack-name loose-touch-staging --region eu-west-3`_


## Project structure.
The [project](https://github.com/straumat/loose-touch) is composed of the following sub projects :
 * loose-touch-server : back end.