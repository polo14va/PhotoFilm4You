context('redirect-from-localhost', () => {
    //Visiting localhost, redirect to localhost/catalog
    describe('Home page in localhost', () => {
        it('should redirect to localhost/catalog', () => {
            cy.visit('/'); // home page is at root

            // Should redirect to localhost/catalog
            cy.url().should('include', '/catalog');
        });
    });

    //Visiting localhost:80, redirect to localhost/catalog
    describe('Home page in localhost:80', () => {
        it('should redirect to localhost/catalog', () => {
            cy.visit('http://localhost:80'); // home page is at root

            // Should redirect to localhost/catalog
            cy.url().should('include', '/catalog');
        });
    });

})