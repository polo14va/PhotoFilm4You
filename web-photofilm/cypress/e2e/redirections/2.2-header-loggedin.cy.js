context('Header Logged in Redirections', () => {
    let mockUser;

    beforeEach(() => {
        // Clear sessionStorage to simulate no USER item
        // Mock user data
        mockUser = {
            id: 1,
            fullName: "USERTEST",
            email: "USERTEST@g.c",
            role: "USER",
            phoneNumber: "666666666"
        };

        // Set USER in sessionStorage
        sessionStorage.setItem('USER', JSON.stringify(mockUser));

        cy.visit('/');
    })

    //Check user exists in localstorage
    describe('When Logged in', () => {
            it('should have an item USER in sessionStorage', () => {
                // Verify that user exists in sessionStorage
                cy.window().then((window) => {
                    const userFromSessionStorage = window.sessionStorage.getItem('USER');
                    expect(userFromSessionStorage).to.not.be.null;
                    const user = JSON.parse(userFromSessionStorage);
                    expect(user.id).to.equal(mockUser.id);
                });
            })
        })
        /*
            //On click on PEDIDOS, redirect to login
            describe('Click on PEDIDOS', () => {
                it('should redirect to localhost/orders', () => {
                    // Should be a link that contains "PEDIDOS"
                    cy.contains('a', 'PEDIDOS')
                        .should('exist')
                        .click(); // Click on the button

                    // After the click, verify the URL has changed to /orders
                    cy.url().should('include', '/orders');
                });
            });
        */
        /*
            //On click on shopping cart image, redirect to shopping-cart
            describe('Click on shopping cart image', () => {
                it('should redirect to localhost/shopping-cart', () => {
                    // Should be a link that contains an image of a shopping cart
                    cy.get('a.nav-link img[alt="Carrito"]').click();

                    // After the click, verify the URL has changed to /shopping-cart
                    cy.url().should('include', '/shopping-cart');
                });
            });
        */

    describe('USER options', () => {
        //On hover USER, should display a dropdown
        it('on hover USER, should display a dropdown', () => {
            // Hover over the dropdown button
            cy.get('.dropdown button').trigger('mouseenter');
            // Verify that the dropdown is displayed
            cy.get('.dropdown-menu').should('be.visible');
        });

        //if role is user should contain a list with 2 elements
        it('If role is user should contain a list with 2 elements', () => {
            // Verify the dropdown contains 2 elements
            cy.get('.dropdown-menu .dropdown-item').should('have.length', 2);
        });

        //if role is admin should contain a list with 3 elements
        it('If role is admin should contain a list with 3 elements', () => {
            // Update user role to ADMIN
            const adminUser = {...mockUser, role: 'ADMIN' };
            sessionStorage.setItem('USER', JSON.stringify(adminUser));
            cy.reload();
            // Verify the dropdown now contains 3 elements
            cy.get('.dropdown-menu .dropdown-item').should('have.length', 3);
        });

        //on click on Cerrar sesión should remove USER from sessionStorage and redirect to catalog
        it('On click Cerrar sesión should remove USER from sessionStorage and redirect to /catalog', () => {
            // Hover over the dropdown button
            cy.get('.dropdown button').trigger('mouseenter');
            // Click on the "Cerrar sesión" dropdown item
            cy.get('.dropdown-menu .dropdown-item').contains('Cerrar sesión').click();
            // Verify the URL has changed to /user
            cy.url().should('include', '/catalog');
            // sessionStorage must not contain the item USER
            cy.window().then((window) => {
                const userFromSessionStorage = window.sessionStorage.getItem('USER');
                expect(userFromSessionStorage).to.be.null;
            });
        });
    });
})