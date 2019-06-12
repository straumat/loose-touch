# [Loose touch](http://www.loosetouch.com)
_Loose touch reminds you to keep in touch with your key contacts_

## Run the application.
You can execute the application with the command `mvn spring-boot:run -Dspring-boot.run.arguments=--start-local-dynamodb,--create-dynamodb-tables -Dspring-boot.run.profiles=local-dynamodb`.

Once packaged, you can also run the docker image `docker run -it -p 8080:8080 --net=host loose-touch/api:0.2-SNAPSHÒT` but you will have to launched a DynamoDB docker image first with the command `̀docker run  -p 8000:8000 amazon/dynamodb-local`.

The project includes a `aws_configuration.yaml` file. You can validate your sam file locally with the command `sam validate -t aws-configuration.yml`._
