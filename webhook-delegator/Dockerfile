FROM ruby:2.6.1-stretch
WORKDIR /opt/webhook-delegator
COPY . /opt/webhook-delegator
RUN bundle install
#FIXME: Replace with a makefile which runs tmp:clear first
RUN rm -rf /opt/webhook-delegator/tmp
CMD rails tmp:clear && rails s 
