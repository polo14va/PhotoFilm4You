context('Catalog Network Requests', () => {
    beforeEach(() => {
        cy.visit('localhost/catalog')
    })

    // Reading session
    describe('Reading session', () => {
        it('should display the alert button when an user is logged in', () => {
            // Mock user data
            const mockUser = {
                id: 1,
                fullName: "USERTEST",
                email: "USERTEST@g.c",
                role: "USER",
                phoneNumber: "666666666"
            };

            // Set USER in sessionStorage
            sessionStorage.setItem('USER', JSON.stringify(mockUser));

            // Reload session to active ngOnInit and leerSesion
            cy.reload();

            // Verify that user exists in sessionStorage
            cy.window().then((window) => {
                const userFromSessionStorage = window.sessionStorage.getItem('USER');
                expect(userFromSessionStorage).to.not.be.null;
                const user = JSON.parse(userFromSessionStorage);
                expect(user.id).to.equal(mockUser.id);
            });

            // Check button with id alert-btn exists and not have class d-none
            cy.get('#alert-btn').should('exist').should('not.have.class', 'd-none');
        });

        it('should not display the alert button when an user is not logged in', () => {
            // Remove USER de sessionStorage
            sessionStorage.removeItem('USER');

            // reload to active ngOnInit y leerSesion
            cy.reload();

            // Verify that user not exists in sessionStorage
            cy.window().then((window) => {
                const userFromSessionStorage = window.sessionStorage.getItem('USER');
                expect(userFromSessionStorage).to.be.null;
            });

            // Check button with id alert-btn exists and one of his class is d-none
            cy.get('#alert-btn').should('exist').should('have.class', 'd-none');
        });
    });

    //Fetching products
    describe('Products Network Requests', () => {
        it('should fetch products from the server', () => {
            // Make a request to fetch products
            cy.request('localhost:18081/products/').then((response) => {
                // Assert that the response status is 200
                expect(response.status).to.equal(200);

                // If the response body is not null, assert that it matches the expected format
                if (response.body) {
                    expect(response.body).to.have.length.greaterThan(0); // Assuming at least one product is returned

                    const firstProduct = response.body[0];
                    expect(firstProduct).to.have.property('id');
                    expect(firstProduct).to.have.property('name');
                    expect(firstProduct).to.have.property('description');
                    expect(firstProduct).to.have.property('dailyPrice');
                    expect(firstProduct).to.have.property('brand');
                    expect(firstProduct).to.have.property('model');
                    expect(firstProduct).to.have.property('categoryId');
                }
            });
        });
    })

})