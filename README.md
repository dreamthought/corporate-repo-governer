# Corporate Repository Governance

# Introduction
We assume that our client has a similar use case to Large Corp who prefer to have their development teams conform to a meaningful taxonomy when creating repositories. 

It has been observed that years of _free for all_ have resulted in it being very hard to answer the following questions:
* Which team or bounded context owns a repository?
* What type of software is this? Is it a prototype, a utility, a library, a web ui, a webservice or a monolith?
* What primary language is it implemented in?

While much of this can be infered from looking at the project or enforced through best efforts, Large Corp have observed that this doesn't often happen.

They would like to police this in a gentle way at the earliest possible moment.


# Structure

This repository may be considered a mono-repository of sorts with two interdependent microservices. I would usually keep them totally decoupled or use subtrees between repositories if I had to, however this structure suits the illustrative purpose of this exercise. The services are split along single responsibility lines:
* A minimally blocking webhook listener and new repository event dispatcher. This also filters for domain events we care about.
* An asynchronously processing Java service which validates repository names.

# Playing along

* To play along you can incrementally checkout specific branches.

# What are we going to build?

![diagram](/docs/images/sequence-diagram.png)

I am going to take you on a journey which solves Large Corp's problem:
1. Implementing a webhook listening for repository creation.
1. Delegating the responsibily of checking naming conformity to another service
1. Asynchronously creating an issue which reminds the maintainers to correct this.

#Part 1 Creating a Webhook

We are going to create a webhook which allows us to inspect the shape of events fired by github when creating a repository.

These are also documented [here](https://developer.github.com/v3/activity/events/types/#repositoryevent)

## Create scafold
Assuming that LargeCorp use Rails and prefer it in their stack, we've created a  minimal rails application to serve API requests, with:
`rails new --api --skip-active-record --skip-active-storage webhook-delegator`

This will provide an http service which listens for incoming HTTP requests and delegates them to another service which validates the repository name and creates an issue if required.

Webhooks provide a notification which is ideally non-blocking, thus handling this asynchronously is a good idea.

We will evolve a design which leads to this.

## A webhook echo service

* Implement a controller which prettyprints the webhook and proves integration.
* _Given more time, I'd usually test to a contract first_

## Create a webhook

* Assuming you have an organization, LargeCorp probably does, you can then create a github app (which appears to be the advised way of handling fine-grained access tokens). You may do so [here](https://github.com/organizations/dreamthought/settings/apps)
* Your rails app will need to be publically accessible by github. For expediency I have used ngrok in front of a docker container with my rails app, to mitigate the security risk of ngrok 
* _Note that I had planned to use the alpine container but ended up using a larger one so that I could bundle install direclty on the container. It's usually a better idea to have a separate build container and a shared volume._
* You can spin up the echo application using `docker-compose up` from the root level folder. This will expose port 3000, which can be further exposed using ngrok, should you choose to use this approach.
* Running `rails cucumber` in the webhook-dispatcher will result in running some simple integration tests. What we promise to do in the language of LargeCorp's Governance Team is to fulfill the following scenario:

  Feature: I want to know about newly created repositories  
    So that they may be checked for compliance  
    For LargeCorp's legal team  

    Scenario: Ensuring that github receives an appropriate response from us

        Given that the TestInc organisation exists in Github

        When we are told that TestInc has a new repository  (the webhook would get called)

        Then we should respond appropriately (we respond with a 204)

* *Note that you would want to ensure that you whitelist githubs source ip on this request and utilise a shared secret when configuring the webhook*

To continue following along `git checkout PART2-handle-change-events`

## Part 2 - Handling Change Events

Before we get going again, ensure that your github app is installed for your oganisation.

* You should visit https://<span></span>github.com/organizations/*YOUR ORG/apps

Replace "YOUR ORG", as required*

* Ensure that you are subscribed to "Repository" events
* Run the app with `docker-compose up --build` and set up ngrok tunnels (you may also expose it publically using any other means) 
* Create a new repository.
* You will see a payload with the following structure come through:

  "repositories\_added": [
    {
      "id": 169968345,
      "node_id": "MDEwOlJlcG9zaXRvcnkxNjk5NjgzNDU=",
      "name": "repo_creation_webhook_test",
      "full_name": "dreamthought/repo_creation_webhook_test",
      "private": false
    }
  ],
  
* We have implemented an entity which encapsulates a github entity in `GithubEntity` and unit tests to ensure that this recognises repository\_added events. Since this is a conceptual demonstration it is fairly make shift, but you may view the spec tests to understand its contract.

To continue following along have a look at PART3-poc-create-issue-in-rails-app

## PART3 Create Issues in GitHub

We're now going to switch gears and build a spring microservice which has the single responsibility of validating the name of a newly created repository and inform the creator of any violations via a github issue. We assume that our large corporate partner, LargeCorp, has JEE development going on, so we'll implement this portion in Spring.

We'll be using github-app token signing as this is a standalone server-to-server process without any individual identity.

### Setup 
* To get started you'll need to download the private key of your app and make a note of its app id from  https://<span/>github.com/organizations/*YOUR ORG*/apps We'll need this later in order to generate a JWT
* Install spring's cli (Java not RoR) (You can use [sdkman](https://sdkman.io) for this)
* In order for health monitoring of services, we use spring-boot actuator. Spring it up with `docker-compose up --build` and connect to *youhost*:8080/actuator/info
* We have defined some of our expected behaviour in `find . -name '*.feature'` although the cucumber tests have not yet been completed due to time constraints.
* To run the full stack:
  * Export the following environmental with your APP\_ID: `GITHUB_APP_ID=1234` Be sure to substitute in your own app id
  * You should also copy your pem file into the pem directory in the repo-name-governor project
  * Spin it up again with `docker-compose up --build`
* Visit [http://localhost:8080/swagger-ui.html#/repository-validation-controller] to experiment with sending payloads to Github
* This stack should respond to create events and attempt to create an issue; you'll currently see a 401 in the docker logs due to JWT signing issues.


# Caveats

* Since this is a server to server application for a corporate entity, I chose to use a github APP and signed JWT tokens generated from the RSA key provided by github. Sadly due to the structure of this, I've had trouble getting it imported into the key store. Due to library support I can't easily work with the base64 decoded varient either. I've looked at the sources of two public open source projects providing Java GitHub API's [github-api](https://github.com/kohsuke/github-api.git) and [jcabi-github](https://github.com/jcabi/jcabi-github). Both of these appear to aovid dealing with JWTs; clearly an area where JVM using partners may have issues. Given a little longer, I'm sure I would have found a work around.

While in retrospect I could have hugely simplified this by implementing everything in a synchronous rails app, we would not have learned about the Java developer experience when working with server-side GitHub apps using JWT. :)


Give me a shout if you have questions at raf AT dreamthought.com
