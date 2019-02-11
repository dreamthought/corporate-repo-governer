require("pp")
require_relative("../models/github_event")
require_relative("../services/name_governance_client")

class WebhookController < ApplicationController
    include NameGovernanceClient

    # Simple echo to validate
    def create
        @body = request.body.read

        @event = GithubEvent.new(@body)

        if @event.isRepositoryCreateEvent?
            # Notify the name governance service
            sendNotification(@event.getRepositoryName)
        end

        # allow response inspection
        if ENV["SNOOP"]
            pp @body
        end
    end
end

