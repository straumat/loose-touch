# Infrastructure installation.
 * Create a aws account.
 * Create an IAM user with access to the API and with the `AdministratorAccess`.
 * Configure your credentials locally with the command `aws configure`.
 * Create a bucket with the command : `aws s3 mb s3://loose-touch-configuration --region eu-west-3` (Do it if it does not exists yet).
 * Add a life cycle rule cleaning the bucket files after 2 days.

# DNS Configuration.
In [AWS Route 53](https://console.aws.amazon.com/route53/home), create an hosted zone and retrieve the name servers list. In google domains, set the name ervers as personalized sever names.

# SSL Certificate creation.
You can create a free certificate in [AWS Certificate Manager](https://console.aws.amazon.com/acm/home?region=us-east-1#/), make sure to ask a certificate for `*.loosetouch.com` in the region `us-east-1` (required by cloudfront).