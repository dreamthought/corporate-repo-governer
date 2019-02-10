require_relative '../../app/models/github_event.rb'
require 'rails_helper'
require ('pp')
RSpec.describe 'The GitHubEvent Entity', :type => :request do
    context  do
        it 'Can be constructed with a valid json body' do
            under_test = GithubEvent.new("{}") 
            expect(under_test).to be_truthy
        end
        it 'Recognises an Add Repository event' do
            @@create_repo_fixture = file_fixture("create-repo-installed-app.json").read
            under_test = GithubEvent.new(@@create_repo_fixture) 
            expect(under_test.isRepositoryCreateEvent?).to be true
        end

        it 'Recognises events which are not Add Repository events' do
            @@remove_repo_fixture = file_fixture("remove-repo-installed-app.json").read
            under_test = GithubEvent.new(@@remove_repo_fixture)
            expect(under_test.isRepositoryCreateEvent?).to be false 
        end
 
    end
end

