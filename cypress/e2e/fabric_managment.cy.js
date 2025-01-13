describe('Fabric Management as Admin', () => {
    beforeEach(() => {
      cy.visit('/authentication');
  
      cy.intercept('POST', '/tokens/login', {
        statusCode: 200,
        body: { accessToken: 'adminAccessToken' },
      }).as('adminLogin');
  
      cy.get('input[placeholder="Enter your username"]').type('test1@test.com');
      cy.get('input[placeholder="Enter your password"]').type('test1@test.com');
      cy.get('button[type="submit"]').click();
  
      cy.wait('@adminLogin');
      cy.url().should('include', '/catalogue');
  
      cy.visit('/fabric-management');
      cy.url().should('include', '/fabric-management');
  
      cy.intercept('GET', '/fabrics*', {
        statusCode: 200,
        body: {
          fabrics: [
            {
              id: 1,
              name: 'Cotton Fabric',
              description: 'Soft cotton fabric',
              material: 'COTTON',
              color: 'WHITE',
              price: 12.99,
              washable: true,
              ironed: false,
              stock: 50,
            },
          ],
          total: 1,
        },
      }).as('fetchFabrics');
    });
  
    it('Adds a fabric', () => {  
      cy.intercept('POST', '/fabrics', {
        statusCode: 201,
        body: {
          id: 2,
          name: 'Silk Fabric',
          description: 'Smooth silk fabric',
          material: 'SILK',
          color: 'RED',
          price: 25.99,
          washable: false,
          ironed: true,
          stock: 30,
          pictureUrl: null,
        },
      }).as('addFabric');
  
      cy.contains('Add Fabric').click();
  
      cy.get('input[placeholder="Name"]').type('Silk Fabric');
      cy.get('textarea[placeholder="Description"]').type('Smooth silk fabric');
      cy.get('#material-select').select('SILK');
      cy.get('#color-select').select('RED');
      cy.get('input[placeholder="Price"]').type('25.99');
      cy.get('input[placeholder="Stock Quantity"]').type('30');
      cy.get('input[type="checkbox"]').eq(1).check(); // Select "Ironed"
  
      cy.get('.add-fabric-submit').click();
  
      cy.wait('@addFabric');
      cy.contains('Fabric added successfully!').should('be.visible');
  
      cy.contains('Silk Fabric').should('be.visible');
    });
  
    it('Edits a fabric', () => {
        cy.intercept('PUT', '/fabrics/1', {
          statusCode: 200,
          body: {
            id: 1,
            name: 'Updated Cotton Fabric',
            description: 'Updated soft cotton fabric',
            material: 'COTTON',
            color: 'BLUE',
            price: 15.99,
            washable: true,
            ironed: true,
            stock: 60,
            pictureUrl: null,
          },
        }).as('editFabric');
      
        cy.contains('Cotton Fabric').should('be.visible');
      
        cy.contains('Cotton Fabric').parent().find('.edit-button').click();
      
        cy.contains('Edit Fabric').should('be.visible');
      
        cy.get('input[placeholder="Name"]').clear().type('Updated Cotton Fabric');
        cy.get('textarea[placeholder="Description"]').clear().type('Updated soft cotton fabric');
        cy.get('#color-select').select('BLUE');
        cy.get('input[placeholder="Price"]').clear().type('15.99');
        cy.get('input[placeholder="Stock Quantity"]').clear().type('60');
        cy.get('input[type="checkbox"]').eq(1).check();
      
        cy.get('button[type="submit"]').contains('Update').should('be.visible').click();
      
        cy.wait('@editFabric');
        cy.contains('Fabric updated successfully!').should('be.visible');
        cy.contains('Updated Cotton Fabric').should('be.visible');
      });      
  
    it('Deletes a fabric', () => {
      cy.intercept('DELETE', '/fabrics/1', {
        statusCode: 200,
        body: { message: 'Fabric deleted successfully!' },
      }).as('deleteFabric');
  
      cy.contains('Cotton Fabric').should('be.visible');
  
      cy.contains('Cotton Fabric').parent().find('.delete-button').click();
  
      cy.get('.swal2-confirm').click();
  
      cy.wait('@deleteFabric');
      cy.contains('Fabric deleted successfully!').should('be.visible');
  
      cy.contains('Cotton Fabric').should('not.exist');
    });
  });  