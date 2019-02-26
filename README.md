# [Loose touch](http://www.loosetouch.com)
_Loose touch reminds you to keep in touch with your key contacts_

## Run the application.
You can execute the application with the command `mvn spring-boot:run -Dspring-boot.run.arguments=--start-local-dynamodb -Dspring-boot.run.profiles=local-namodb`.

## Run the project.
The project folder includes a `aws_configuration.yaml` file. 

You can use this [SAM](https://github.com/awslabs/serverless-application-model) file to deploy the project to AWS Lambda and Amazon API Gateway with the command : `./deploy environement` where environment is `test`, `stage` or `production`. _note : you can delete a whole stack with the command ̀`aws cloudformation delete-stack --stack-name STACK_NAME --region eu-west-3` (where `̀STACK_NAME` is `loose-touch-test`, `loose-touch-stage` or `loose-touch-production`)._
 
_note : you can validate your sam file locally with the command `sam validate -t aws-configuration.yml`._

## Project structure.
The [project](https://github.com/straumat/loose-touch) is composed of the following sub projects :
 * loose-touch-server : back end.