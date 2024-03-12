context('Register Network Requests', () => {
    beforeEach(() => {
        cy.visit('localhost/register')
    })

    // Invalid input data
    describe('Fill form with invalid data', () => {
        //name
        it('should display warning for pristine name', () => {
            //pristine name
            cy.get('#fullName').type('xxx');
            cy.get('#fullName').clear();
            // Check if the 'required' error alert is visible
            cy.get('#err-name-required').should('be.visible');
        })
        it('should display warning for less than 5 char in name', () => {
            // Insert incorrect name that triggers the 'minlength' error
            cy.get('#fullName').type('abcd'); // Assuming the minimum length is 5 characters
            // Check if the 'minlength' error alert is visible
            cy.get('#err-name-atleast5char').should('be.visible');
        })
        it('should display warning for invalid name', () => {
                // Insert incorrect name that triggers the 'pattern' error
                cy.get('#fullName').type('12345'); // Assuming a pattern validation that doesn't allow numbers
                // Check if the 'pattern' error alert is visible
                cy.get('#err-name-notvalid').should('be.visible');
            })
            //email
        it('should display warning for pristine email', () => {
            //pristine email
            cy.get('#email').type('xxx');
            cy.get('#email').clear();
            // Check if the 'required' error alert is visible
            cy.get('#err-email-required').should('be.visible');
        })
        it('should display warning for invalid email', () => {
            cy.get('#email').type('invalidemail');
            cy.get('#err-email-notvalid').should('be.visible');
        })
        it('should display warning for invalid email', () => {
            cy.get('#email').type('missingatdomain.com');
            cy.get('#err-email-notvalid').should('be.visible');
        })
        it('should display warning for invalid email', () => {
            cy.get('#email').type('@missingusername.com');
            cy.get('#err-email-notvalid').should('be.visible');
        })
        it('should display warning for invalid email', () => {
                cy.get('#email').type('special@characters!notallowed.com');
                cy.get('#err-email-notvalid').should('be.visible');
            })
            //password
        it('should display warning for pristine password', () => {
            cy.get('#password').type('xxx');
            cy.get('#password').clear();
            cy.get('#err-password-required').should('be.visible');
        })
        it('should display warning for having less than 8 chars password', () => {
            cy.get('#password').type('1234567');
            cy.get('#err-password-atleast8char').should('be.visible');
        })
        it('should display warning for not matching pattern password', () => {
            cy.get('#password').type('abcdefgh');
            cy.get('#err-password-notvalid').should('be.visible');
        })
        it('should display warning for not matching pattern password', () => {
            cy.get('#password').type('ACBDefgh');
            cy.get('#err-password-notvalid').should('be.visible');
        })
        it('should display warning for not matching pattern password', () => {
            cy.get('#password').type('1234abcd');
            cy.get('#err-password-notvalid').should('be.visible');
        })
        it('should display warning for not matching pattern password', () => {
                cy.get('#password').type('1234ACBD');
                cy.get('#err-password-notvalid').should('be.visible');
            })
            //phone number
        it('should display warning for pristine password', () => {
            cy.get('#phoneNumber').type('123');
            cy.get('#phoneNumber').clear();
            cy.get('#err-phone-required').should('be.visible');
        })
        it('should display warning for not valid spanish number', () => {
            cy.get('#phoneNumber').type('000');
            cy.get('#err-phone-notvalid').should('be.visible');
        })
        afterEach(() => {
            // Check if the submit button is disabled after each test
            cy.get('button[type="submit"]').should('be.disabled');
        });
    })

    // Fill form
    describe('Fill form and click Registrarse', () => {
            it('should fill every field correctly and submit button enabled', () => {
                // Fill in the form fields
                cy.get('#fullName').type('Testing');
                cy.get('#email').type('testing@test.com');
                cy.get('#password').type('Password123');
                cy.get('#password2').type('Password123');
                cy.get('#phoneNumber').type('987654321');
                //check submit button not dissabled
                cy.get('button[type="submit"]').should('not.be.disabled');
            });
        })
        /*
            // New user signing in the ddbb
            describe('New user registering', () => {
                    // Intercept the registration API call and control the response
                    cy.server();
                    cy.route({
                        method: 'POST',
                        url: 'http://localhost:18082/users/create', // Replace with your actual API endpoint
                        status: 200,
                        response: {},
                    }).as('successfulRegistration');

                    it('should add the user in the ddbb', () => {

                    });
                    it('should get 200 status', () => {

                    });
                    it('should display success alert message', () => {

                    });
                    it('should redirect to /login', () => {

                    });
                })
                // User already exists in the ddbb
                /*
                describe('User already exists in the ddbb', () => {
                    it('should fill every field correctly', () => {
                        
                    });
                })
                */
})