// ***********************************************************
// This example support/index.js is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

// Import commands.js using ES2015 syntax:
import "./commands";

// Alternatively you can use CommonJS syntax:
// require('./commands')

// reset db
beforeEach(() => {
  cy.request(
    "DELETE",
    "http://localhost:8081/emulator/v1/projects/word-penne/databases/(default)/documents"
  );
});

Cypress.on("uncaught:exception", (err, _) => {
  expect(err.message).to.include(
    "Could not find the FirebaseUI widget element on the page."
  );
  done();

  return false;
});
