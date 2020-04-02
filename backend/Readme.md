# epitrack - Epidemiemanagement für Gesundheitsbehörden

Epidemiemanagement für Gesundheitsbehörden durch Digitalisierung von Selbstauskünften bestätigter Quarantänefälle und Verdachtspersonen (inkl. Informationen zu Symptomen und Kontakten)

See our [intro video](https://www.youtube.com/watch?time_continue=120&v=z__mJRP8O0w&feature=emb_logo)!

## Status
 This is a project in development. It has been created during the [WirVsVirusHackathon](https://wirvsvirushackathon.org/).
  

## Development Setup
Initial DB setup
* run docker-compose up
* connect to the database container `docker exec -it <CONTAINER_ID> bash`
* `su - postgres`
* `createuser --interactive --pwprompt`
    * rolename : `corona-report-app`
	* password: `corona`
	* superuser: yes
* `createdb -O corona-report-app corona-report`

## Dummy Data
Two health departments are created as dummy data upon startup with the following id and passcode:
Testamt1 aba0ec65-6c1d-4b7b-91b4-c31ef16ad0a2
Testamt2 ca3f3e9a-414a-4117-a623-59b109b269f1