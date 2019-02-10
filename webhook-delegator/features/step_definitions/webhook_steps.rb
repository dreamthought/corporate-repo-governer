Given("that the TestInc organisation exists in Github") do
    # Ensure we have TestInc in our fixture
end

When("we are told that TestInc has a new repository") do
    # Send the fixture
    post "/webhook", body: {repository: {}}
end

Then("we should respond appropriately") do
    # Ensure we get a status 204
    expect(last_response.status).to eq 204
end

