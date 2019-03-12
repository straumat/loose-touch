## Project release guide.
In a shell :
 * Type `git remote -v` and check that you are in ssh and not https.
 * If you are in https, type : `git remote set-url origin git@github.com:straumat/loose-touch.git`.
 * Start the release with the command : `mvn gitflow:release-start`.
 * Finish the release with the command : `mvn gitflow:release-finish`.
 * Go back to the development branch with the command : `git checkout development`.

_note: If the plugin did not push everything, run : `git push --tags;git push origin master;git push origin development`._