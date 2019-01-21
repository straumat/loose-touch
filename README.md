# [Loose touch](http://www.loosetouch.com)
_Loose touch helps you remind to keep in touch with key contacts_

## Application infrastructure.
 * [Amazon CloudFront](https://aws.amazon.com/en/cloudfront/).
 * [AWS Lambda](https://aws.amazon.com/en/lambda/).
 * [Amazon DynamoDB](https://aws.amazon.com/en/dynamodb/). 

## Development infrastructure.
 * [Github](https://github.com/straumat/loose-touch).
 * [Dependabot](https://dependabot.com/).

## Frameworks.
 * [Maven](https://maven.apache.org/).
 * [Spring boot](https://spring.io/projects/spring-boot).
 * [AWS Serverless Java Container](https://github.com/awslabs/aws-serverless-java-container).

## Project organization.
The [project](https://github.com/straumat/loose-touch) is composed of the following sub projects :
 * loose-touch-api : contains the rest API.
 * loose-touch-batch : contains application batch.
 * loose-touch-chrome-extension : contains the chrome extension.
 * loose-touch-web-site : contains the public web site deployed to S3.

# Project release.
In a shell :
 * Type `git remote -v` and check that you are in ssh and not https.
 * If you are in https, type : `git remote set-url origin git@github.com:straumat/loose-touch.git`.
 * Start the release with the command : `mvn gitflow:release-start`.
 * Finish the release with the command : `mvn gitflow:release-finish`.
 * If the plugin did not push everything, run : `git push --tags;git push origin master;git push origin development`.
 * Go back to the development branch with the command : `git checkout master`.