# Quarano project

### Useful links
1. Einladung zum https://quarano.atlassian.net (Jira und Confluence)
2. Einladung zum Slack https://quarano.slack.com
  - Pinned Articles in Slack beachten
3. Links:
  - https://www.quarano.de (Landingpage zum Produkt)
  - https://github.com/quarano/quarano-application (Repository)
  - https://www.docker.com/products/docker-desktop Docker (falls gewünscht)
4. LOCAL Entwicklung
  - http://localhost:8080/docs/index.html (erzeugte Doku Backend)
  - http://localhost:4200/auth/login (Login Frontend)
5. DEV und TEST Umgebung
  - https://quarano.de/internal-dev
  - https://quarano.de/internal-test
6. Google Drive Dokumentenfreigabe ggf. wenn Deployment und Operations inkl.
  - Cloud Foundry basierend, z.Z. in OTC von der Deutschen Telekom gehostet

### Development
IntelliJ Idea useful plugins:
- Eclipse Code Formatter (code_formatter.xml und quarano.importorder unter infrastructure/ in das Plugin importieren)
- Markdown (für md Dateien, in neueren IntelliJ standardsmäßig installiert)
- AsciiDoc (für adoc Dateien)
- Lombok (Annotation Processing und Lombok Plugin für das Java Projekt aktivieren)

Umgebung vorbereiten:
- Backend: JDK 11, Maven, IDE
- Frontend:
  - nodejs Umgebung (mit npm) -> Umgebungsvariablen für das eigene Windows Konto entsprechend anpassen
  - Angular Setup (https://angular.io/guide/setup-local)
- Readmes im Projektverzeichnis beachten

### Project workflow
Ablauf zum Umsetzen einer neuen Story:
1. Ihr sucht euch einen Backend / Frontendpartner für eine Story und erstellt einen Featurebranch im Git (**feature/CORE-{Ticketnummer}-{Titel}**)
2. Ihr schiebt die Story "in Bearbeitung" und einer von euch trägt sich als Bearbeiter/ Assignee ein
3. Ihr zerlegt die Story in Subtasks für Frontend und backend ganz so wie es euch hilft
4. Der Worklflow für die Subtasts ist ganz simple: todo - in Bearbeitung - done
5. Wenn Frontend oder backend fertig ist stellt ihr über Github einen Pull Request und nominiert einen Reviewer
6. Parallel gerne schon einmal fachliches Feedback von Thorsten oder mir einholen
7. Wenn alles gereviewed ist, prüft  das Frontend/Backend-Duo noch einmal gemeinsam ob alle Akzeptanzkriterien der Userstory bedacht wurden und schiebt die Story in Absprache mit Matthias auf den Testserver und setzt den Status auf "Bereit zum Testen".
8. Dann löscht ihr den remote-feature-Branch

Ablauf für Bugs:
1. Ihr erstellt einen bugfix branch (**bugfix/CORE-{Ticketnummer}-{Titel}**) und setzt den Bug in den Status "in Bearbeitung" und tragt euch als "Bearbeiter / Asssignee" ein
2. ihr löst das Problem
3. ihr merged den bugfix-branch in den develop Branch
4. ihr deployed in Absprache mit Matthias den Fix auf dem Testserver und stellt den Bug auf "Bereit zum Testen"
5. ihr löscht den remote-bugfix-branch

Hinweise zum Workflow:
1. Während man an einem Featurebranch arbeitet, regelmäßig rebasen (einfach im Branch git rebase develop). Wenn dabei Mergekonflikte auftauchen: lösen und mit git rebase --continue fortfahren. Somit is eure Branch immer develop + alle eure anvisierten Änderungen. Diese Branch kann man auch jederzeit mit --force-with-lease pushen. Das ist wie -f, nur freundlicher, weil es nichts kaputt macht, wenn jemand anderes da schon vorher Änderungen reingepusht hat.
2. Vor einem Merge nach develop immer nochmal 1. machen.
3. Mergen mit git merge --ff-only $featurebranch. Das stellt sicher, dass ihr 2. nicht vergessen habt und sorgt dafür das die Commits eures Branches einfach ohne Mergecommit auf develop landen.

Mehrere Pushes per story sind ein Problem mit Tooling/Review, deswegen: Wenn jemand einen Pull Request einreicht, der mehrere Commits enthält, wäre es grandiosigst, wenn die Beschreibung des PullRequest eine Zusammenfassung dessen wäre was in Bezug auf das Ticket, dass der PR lösen soll. Idealerweise kann diese Beschreibung direkt als Commitkommentar für den gesquashten Commit verwendet werden.Die Einzelcommitkommentare wie "Geschäftslogik geändert" sind nur sehr wenig hilfreich, wenn es darum geht zu verstehen was genau passiert ist und warum. Außerdem muss sonst der mergende, sich aus den x Commits dann selbst eine Commitmessage für den gesquashten Commit aus den Fingern saugen, was nicht immer trivial ist. Wenn das der PR Submittende vorbereitet beschleunigt das die Mergearbeit ungemein.


### Assorted notes
- hateoas REST Ansatz, Customizing in HalModelBuilder Klasse
- Domäne > Web Package > Controller
- Datenaustausch zwischen Domänen mit Spring Events -> lose Bindung und "Micro Service Ready"
- Postman Collection in infrastructure/ Verzeichnis
- H2 in Memory DB, Db Name dynamisch pro Instanz und pro Integration Test

### Test users on the local test instance
**Testuser für Registrierung:**
- Indexfall Tanja Müller  bei GA 1 (hat eine Einladung mit folgendem Code bekommen, aber sich aber noch nicht registriert)
  - activationcode / clientcode: acc8b747-1eac-4db4-a8f3-d2a8bbe8320d
  - Geburtsdatum: 03.08.1975

**Testuser für Login:**
- Indexfall Markus Hanser bei GA 1 (hat sich registriert, aber die Basisdatenerfassung noch nicht abgeschlossen)  
  - username: "DemoAccount"
  - passwort: "DemoPassword"
- Indexfall Sandra Schubert bei GA 2 (hat sich registriert und bereits 3 Tagebucheinträge gemacht, allerdings nur einen pro Tag)
  - username: "test3"
  - passwort: "test123"

**Gesundheitsamt Benutzer**
- User bei GA Mannheim
  - username "agent1"
  - passwort: "agent1"
- GA User bei GA Mannheim
  - username: "agent2"
  - passwort: "agent2"
- Admin  bei GA Mannheim
  - username: "admin"
  - passwort: "admin"
- User bei GA Darmstadt
  - username: "agent3"
  - passwort: "agent3"

### Developer notes
- Install frontend dependencies with `npm install`. Dependencies of the backend are installed automatically when you first start the local server
- Start backend with `mvn spring-boot:run -Pinmemory` (if you don't have *mvn* on your *PATH* then you can use a local maven with `./mnvw  spring-boot:run -Pinmemory`), then start frontend with `npm run ng serve` (if *angular-cli* is installed as a global package you can also use the shorter command `ng serve` to start the frontend). After that a local test site will be available at http://localhost:4200
- Run `nx e2e --headless` in frontend directory instead of `ng serve` to test the entire application, both frontend and backend
  - Some e2e tests writes to the database in the backend. So you need to restart the backend before rerunning the test again
  - Headless mode might not work in Windows
- The docs for the backend is at http://localhost:8080/docs/index.html However they aren't generated in the maven build process until after running the integration tests. Use `mvn verify` to generate those docs

- Review process: one reviewer and one approval is sufficient, though more doesn't hurt
  - For Jira issue workflow see the sections above

- Frontend
  - Depending on the OS and how node was installed, npm might ignore command line options starting with `--`. As a workaround: define a new "script" in package.json and run that
  - Generate a new component with `nx g component {component name} --project={parent project name} --export`. A list of all existing projects is in angular.json
  - Generate a new library (in angular called project) with `nx g lib {library name} --directory={save location}`. The "save location" is a directory inside the "libs" folder. Select SASS as stylesheet format. The angular project name will be `{save locaction}-{library name}`
