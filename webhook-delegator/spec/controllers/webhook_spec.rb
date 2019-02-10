require 'rails_helper'

RSpec.describe 'Webhook Delegator Service', :type => :request do

    @controller = WebhookController.new
    describe 'POST /webhook', :type => :request do
        context 'when we have a well-formed and understood request' do
            it 'returns a no-content response' do
                post '/webhook', params: file_fixture("create-repo-installed-app.json").read.to_json
                expect(response).to have_http_status(204)
            end
        end


    end

end

