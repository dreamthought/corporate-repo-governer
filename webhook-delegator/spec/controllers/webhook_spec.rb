require 'spec_helper'
require 'rails_helper'

RSpec.describe 'Webhook Delegator Service', :type => :request do

    def stub_governer
      stub_request(:post, "http://repository-governer/repository/check").
         with(
           headers: {
              'Accept'=>'*/*',
              'Accept-Encoding'=>'gzip;q=1.0,deflate;q=0.6,identity;q=0.3',
              'Host'=>'repository-governer',
              'User-Agent'=>'Ruby'
           }).to_return(status: 202, body: "", headers: {})
    end


    describe 'POST /webhook', :type => :request do
        context 'when we have a well-formed and understood request' do
            it 'returns a no-content response' do
                post '/webhook', params: file_fixture("create-repo-installed-app.json").read.to_json
                expect(response).to have_http_status(204)
            end

            it 'requests a collabor asynchronously checks the repository name' do
                post '/webhook', params: file_fixture("create-repo-installed-app.json").read.to_json
                uri = URI('http://repository-governer/repository/check')
                req = Net::HTTP::Post.new(uri)
                res = Net::HTTP.start(uri.hostname, uri.port) do |http|
                    http.request(req, '{ "repository": "myorg/project1" }')
            
                    response = Net::HTTP.post(uri)
                end

            end

        end
    end

end


