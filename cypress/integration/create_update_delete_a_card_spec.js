describe("Create and update a card", () => {
  it("Visits a root page and sign in", () => {
    cy.visit("/");

    cy.contains("Logout");
  });

  it("Create, update and delete a new card", () => {
    cy.visit("/");

    // language setting
    cy.wait(1000)
    cy.getBySel("user-setting__submit").click({ force: true });

    // show home page
    cy.wait(1000)
    cy.get("header [data-testid=word-card-add-button]").click({ multiple: true, force: true });

    // create a new card
    cy.url().should("include", "/cards/new");
 
    cy.wait(1000)
    cy.getBySel("word-card-form__front").type("make", { force: true });
    cy.getBySel("word-card-form__back").type("作る");
    cy.getBySel("word-card-form__submit").click();

    // show the new card on the top page
    cy.url().should("eq", "http://localhost:8080/");
    cy.contains("make");
    cy.contains("作る");

    // edit the card
    cy.contains("make").contains("edit").click({ force: true });
    cy.wait(1000)
    cy.url().should("include", "edit");
    cy.getBySel("word-card-form__front").type(
      "{backspace}{backspace}{backspace}{backspace}create"
    );
    cy.getBySel("word-card-form__back").type("{backspace}{backspace}創造する");
    cy.getBySel("word-card-form__comment").type("新しく作る");
    cy.getBySel("word-card-form__submit").click();

    // show the edited card on the top page
    cy.contains("make").not();
    cy.contains("作る").not();
    cy.contains("create");
    cy.contains("創造する");
    cy.contains("新しく作る");
    cy.url().should("eq", "http://localhost:8080/");

    // search
    cy.getBySel("search-input").type("cr");
    cy.contains("create");
    cy.getBySel("search-input").type("{backspace}{backspace}ma");
    cy.contains("create").not();
    cy.getBySel("search-input").type("{backspace}{backspace}");
    cy.contains("create");

    // archive
    cy.contains("create").contains("archive").click({ force: true });
    cy.getBySel("navigation__archive").click({ force: true });
    cy.contains("create");

    // delete a card
    cy.contains("create").contains("delete").click({ force: true });
    cy.contains("Confirmation");
    cy.getBySel("confirmation-modal__ok").click();
    cy.contains("create").not();
    cy.contains("創造する").not();
    cy.contains("新しく作る").not();
  });
});
