context('Login Requests', () => {
    beforeEach(() => {
        cy.window().then((win) => {
            win.sessionStorage.clear();
        });
        cy.visit('/login')
    })

    //Redirect to catalog
    describe('Click on X', () => {
        it('should redirect to localhost/catalog', () => {
            // Should be a link that contains "CATÁLOGO"
            cy.contains('button', 'X')
                .should('exist')
                .click(); // Click on the button

            // After the click, verify the URL has changed to /catalog
            cy.url().should('include', '/catalog');
        });
    });

    //Redirect to register
    describe('Click on Quieres regisrarte?', () => {
        it('should redirect to localhost/register', () => {
            // Should be a link that contains "CATÁLOGO"
            cy.contains('a', '¿Quieres registrarte?')
                .should('exist'); // Assuming the link has an href attribute pointing to '/register'

            // On click this link, should redirect to http://localhost/register
            cy.contains('a', '¿Quieres registrarte?').click();

            // After the click, verify the URL has changed to /register
            cy.url().should('include', '/register');
        });
    });

    //Redirect to password
    describe('Click on No recuerdas tu contraseña?', () => {
        it('should redirect to localhost/register', () => {
            // Should be a link that contains "CATÁLOGO"
            cy.contains('a', '¿No recuerdas tu contraseña?')
                .should('exist'); // Assuming the link has an href attribute pointing to '/register'

            // On click this link, should redirect to http://localhost/register
            cy.contains('a', '¿No recuerdas tu contraseña?').click();

            // After the click, verify the URL has changed to /register
            cy.url().should('include', '/password');
        });
    });

})