# [Loose touch](http://www.loosetouch.com)
_Loose touch reminds you to keep in touch with your key contacts_

## Run the application.
You can execute the application with the command `mvn spring-boot:run -Dspring-boot.run.arguments=--start-local-dynamodb -Dspring-boot.run.profiles=local-dynamodb`.

The project includes a `aws_configuration.yaml` file. You can validate your sam file locally with the command `sam validate -t aws-configuration.yml`._