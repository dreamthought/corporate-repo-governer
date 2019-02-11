require 'spec_helper'
require 'rails_helper'

RSpec.describe 'Webhook Delegator Service', :type => :request do

    before(:each) do
       stub_request(:post, "http://repo-name-governor:8080/repository/name/dreamthought/repo_creation_webhook_test").
         with(
           body: "{\"repositoryName\":\"dreamthought/repo_creation_webhook_test\"}",
           headers: {
       	  'Content-Type'=>'application/json; charset=utf-8'
           }).
         to_return(status: 202, body: "", headers: {})
    end

    describe 'POST /webhook', :type => :request do
        context 'when we have a well-formed and understood request' do
            it 'returns a no-content response' do
                post '/webhook', params: file_fixture("create-repo-installed-app.json").read
                expect(response).to have_http_status(204)
            end
        end
    end

end


