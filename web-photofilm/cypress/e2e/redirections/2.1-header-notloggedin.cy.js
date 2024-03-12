context('Header Not Logged in Redirections', () => {
    beforeEach(() => {
        // Clear sessionStorage to simulate no USER item
        cy.window().then((win) => {
            win.sessionStorage.clear();
        });
        cy.visit('/');
    })

    //On click Iniciar sesión, redirect to login component
    describe('Click on Iniciar sesión', () => {
        it('should redirect to localhost/login', () => {
            // Should be a button that contains "Iniciar sesión"
            cy.contains('button', 'Iniciar sesión')
                .should('exist')
                .click(); // Click on the button

            // After the click, verify the URL has changed to /login
            cy.url().should('include', '/login');
        });
    });

    //On click on PEDIDOS, redirect to login
    describe('Click on PEDIDOS', () => {
        it('should redirect to localhost/login', () => {
            // Should be a link that contains "PEDIDOS"
            cy.contains('a', 'PEDIDOS')
                .should('exist')
                .click(); // Click on the button

            // After the click, verify the URL has changed to /login
            cy.url().should('include', '/login');
        });
    });

    /*
        //On click on shopping cart image, redirect to login
        describe('Click on shopping cart image', () => {
            it('should redirect to localhost/login', () => {
                // Should be a link that contains an image of a shopping cart
                cy.get('a.nav-link img[alt="Carrito"]').click();

                // After the click, verify the URL has changed to /login
                cy.url().should('include', '/login');
            });
        });
    */
})