require 'json'

# Represents an event consumed from GitHub
# FIXME: use a json inflated entity or jsonpath
class GithubEvent

    @@ADD_REPO_KEY = "repositories_added"
    @@REPO_NAME_KEY = "full_name"

    def initialize(githubEvent)
        @json = JSON.parse(githubEvent)
    end

    # Since our bounded context is particularly concerned with
    # repos being created
    def isRepositoryCreateEvent? 
        @json.has_key?( @@ADD_REPO_KEY ) && @json[ @@ADD_REPO_KEY ].length >= 1;
    end

    # returns the name of the first created repo in this event
    def getRepositoryName
        if (self.isRepositoryCreate?)
            # we assume there is only one repo per change set for
            # the purpose of this change set.
            # This can be confirmed alter
            return @json[ @@ADD_REPO_KEY ][0][ @@REPO_NAME_KEY ]
        end
    end
end
