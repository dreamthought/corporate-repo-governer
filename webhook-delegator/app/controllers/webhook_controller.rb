class WebhookController < ApplicationController

    # Simple echo to validate
    def create
        @body = request.body.read
        puts @body
    end

end

