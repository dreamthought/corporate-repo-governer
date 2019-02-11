# Corporate Repository Governance

# Introduction
We assume that our client has a similar use case to Large Corp who prefer to have their development teams conform to a meaningful taxonomy when creating repositories. 

It has been observed that years of free for all have resulted in it being very hard to answer the following question:
* Which team or bounded context owns a repository?
* What type of software is this? Is it a prototype, a utility, a library, a web ui, a webservice or a monolith?
* What primary language is it implemented in?

While much of this can be infered from looking at the project or enforced through best efforts, Large Corp have observed that this doesn't often happen.

They would like to police this in a gentle way at the earliest possible moment.

# What are we going to build?

I am going to take you on a journey which solves Large Corp's problem:
1. Implementing a webhook listening for repository creation.
1. Delegating the responsibily of checking naming conformity to another service
1. Asynchronously creating an issue which reminds the maintainers to correct this.

Now ```git checkout PART1-implement-listener```

