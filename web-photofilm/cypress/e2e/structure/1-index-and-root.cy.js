describe('on visiting /catalog (main page)', () => {
    beforeEach(() => {
        cy.visit('/catalog')
    })

    //body container
    //the element body contains a div that containts 2 divs. The firstone is the navbar, the secondone is the router-outlet
    it('should contain some 2 divs, one of them the navbar and the other one the router-outlet', () => {
        // only one body
        cy.get('body').should('have.length', 1)

        // body contains app-root
        cy.get('body > app-root').should('have.length', 1)

        // app-root contains 1 div that contains 2 divs
        cy.get('app-root > div > div').should('have.length', 2)

        // first div contains the app-header element
        cy.get('app-root > div > div')
            .first() // select the first div
            .find('app-header') // assuming 'row' is a class, adjust accordingly
            .should('exist'); // assuming app-header is present in the first div

        // second div contains the router-outlet element
        cy.get('app-root > div > div')
            .last() // select the second div
            .find('router-outlet') // assuming 'row' is a class, adjust accordingly
            .should('exist'); // assuming router-outlet is present in the second div
    })
})