context('Header Redirections', () => {
    beforeEach(() => {
        cy.visit('/')
    })

    //Visiting catalog component, on click catálogo, redirect to catalog component
    describe('Click on CATÁLOGO', () => {
        it('should redirect to localhost/catalog', () => {
            // Should be a link that contains "CATÁLOGO"
            cy.contains('a', 'CATÁLOGO')
                .should('exist')
                .and('have.attr', 'href', '/catalog'); // Assuming the link has an href attribute pointing to '/catalog'

            // On click this link, should redirect to http://localhost/catalog
            cy.contains('a', 'CATÁLOGO').click();

            // After the click, verify the URL has changed to /catalog
            cy.url().should('include', '/catalog');
        });
    });

    //Clicking on PhotoFilm image, redirect to catalog component
    describe('Click on PhotoFilm image', () => {
        it('should redirect to localhost/catalog', () => {
            // Should be a link that contains an image with alt="Logo"
            cy.get('a.navbar-brand img[alt="Logo"]')
                .should('exist')
                .and('have.attr', 'src', 'assets/logo.png') // Assuming the image source is 'assets/logo.png'
                .parent('a')
                .should('have.attr', 'href', '/'); // Assuming the link has an href attribute pointing to '/'

            // On click this link, should redirect to http://localhost/catalog
            cy.get('a.navbar-brand img[alt="Logo"]').click();

            // After the click, verify the URL has changed to /catalog
            cy.url().should('include', '/catalog');
        });
    });

})