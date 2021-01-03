describe("Create and update a card", () => {
  it("Visits a root page and sign in", () => {
    cy.visit("/");

    cy.contains("Logout");
  });

  it("Create and Update a new card", () => {
    cy.visit("/");

    cy.contains("add").click();

    // create a new card
    cy.url().should("include", "/cards/new");

    cy.get("input#front-text").type("make", { waitForAnimations: false });
    cy.get("input#back-text").type("作る");
    cy.contains("Submit").click();

    // show the new card on the top page
    cy.url().should("eq", "http://localhost:8080/");
    cy.contains("make");
    cy.contains("作る");

    // edit the card
    cy.contains("make").contains("edit").click({ force: true });
    cy.url().should("include", "edit");
    cy.get("input#front-text").type(
      "{backspace}{backspace}{backspace}{backspace}create"
    );
    cy.get("input#back-text").type("{backspace}{backspace}創造する");
    cy.get("input#comment").type("新しく作る");
    cy.contains("Submit").click();

    // show the edited card on the top page
    cy.url().should("eq", "http://localhost:8080/");
    cy.contains("make").not();
    cy.contains("作る").not();
    cy.contains("create");
    cy.contains("創造する");
    cy.contains("新しく作る");
  });
});
