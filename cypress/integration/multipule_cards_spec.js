describe("Multiple cards", () => {
    it("search by tags", () => {
        cy.visit("/");

        cy.getBySel("word-card-add-button").click({ multiple: true, force: true });

        // create a new card
        cy.url().should("include", "/cards/new");

        cy.getBySel("word-card-form__front").type("make", { force: true });
        cy.getBySel("word-card-form__back").type("作る");
        cy.getBySel("word-card-form__submit").click();

        // show the new card on the top page
        cy.contains("make");
        cy.contains("作る");

        // make a second card
        cy.getBySel("word-card-add-button").click({ multiple: true, force: true });

        cy.url().should("include", "/cards/new");

        cy.getBySel("word-card-form__front").type("have", { force: true });
        cy.getBySel("word-card-form__back").type("持つ");
        cy.getBySel("tag-name-0").type("基礎");
        cy.getBySel("word-card-form__submit").click();

        cy.contains("have");
        cy.contains("持つ");
        cy.contains("基礎");

        // search by a tag's name
        cy.getBySel("navigation__tags-0").click({ force: true });
        cy.contains("make").not();
        cy.contains("have"); // show only having a tag

        cy.getBySel("navigation__cards").click({ force: true });
        cy.contains("make");
        cy.contains("have");
    });
});
