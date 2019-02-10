# Corporate Repository Governance

# Introduction
We assume that our client has a similar use case to Large Corp who prefer to have their development teams conform to a meaningful taxonomy when creating repositories. 

It has been observed that years of free for all have resulted in it being very hard to answer the following question:
* Which team or bounded context owns a repository?
* What type of software is this? Is it a prototype, a utility, a library, a web ui, a webservice or a monolith?
* What primary language is it implemented in?

While much of this can be infered from looking at the project or enforced through best efforts, Large Corp have observed that this doesn't often happen.

They would like to police this in a gentle way at the earilest possible moment.

# What are we going to build?

I am going to take you a journey which solves evil corp's problem:
1. Implementing a webhook listening for repository creation.
1. Delegating the responsibily of checking naming confomity to another service
1. Asynchronously creating an issue which reminds the maintainers to correct this.

#Part 1 Creating a Webhook

We are going to create a webhook which allows us to inspect the shape of events fired by github when creating a repository.

These are also documted [https://developer.github.com/v3/activity/events/types/#repositoryevent here]

## Create scafold
Assuming that LargeCorp use Rails and prefer it in their stack, we've created a  minimal rails application to serve API requests, with:
`rails new --api --skip-active-record --skip-active-storage webhook-delegator`

This will provide an http service which listens for incoming HTTP requsts and delegates them to another service which validates the repository name and creates an issue if required.

Webhooks provide a notificaiton which is ideally non-blocking, thus handling this asynchronously is a good idea.

We will evolve a design which leads to this.

## A webhook echo service

* Implement a controller which prettyprints the webhook and proves integration.
* _Given more time, I'd usually test to a contract first_

## Create a webhook

* Assuming you have an organization, LargeCorp probably does, create you can then create a github app (which appears to be the advised way of handling fine-grained access tokens). You may do so [https://github.com/organizations/dreamthought/settings/apps here]
* Your rails app will need to be publically accessible by github. For expediency I have used ngrok in front of a docker container with my rails app, to mitigate the security risk of ngrok 
* _Note that I had planned to use the alpine container but ended up using a larger one so that I could bundle install direclty on the container. It's usually a better idea to have a separate build container and a shared volume._
* You can spin up the echo application using `docker-compose up` from the root level folder. This will expose port 3000, which can be further exposed using ngrok, should you choose to use this approach.
* Running `rails cucumber` in the webhook-dispatcher will result in running some simple integration tests. What we promise to do in the language of LargeCorp's Governance Team is to fulfill the following scenario:

  Feature: I want to know about newly created repositories  
    So that I they may be checked for compliance  
    For LargeCorp's legal team  

    Scenario: Ensuring that github recieves an appropriate response from us

        Given that the TestInc organisation exists in Github

        When we are told that TestInc has a new repository  _the webhook would get called_

        Then we should respond appropriately _we respond with a 204_

* *Note that you would want to ensure that you whitelist githubs source ip on this request and utilise a shared secret when configuring the webhook*

To continue following along `git checkout PART2-handle-change-events`


