describe('Personal Information Page', () => {
    const mockUser = {
      id: 11,
      username: 'testuser',
      email: 'testuser@example.com',
      firstName: 'Test',
      lastName: 'User',
    };
  
    const setMockAuth = () => {
      const token = 'mockAccessToken';
      const claims = JSON.stringify({ userId: mockUser.id });
  
      cy.window().then((win) => {
        win.localStorage.setItem('accessToken', token);
        win.localStorage.setItem('claims', claims);
      });
    };
  
    beforeEach(() => {
      setMockAuth();
  
      cy.intercept('GET', '**/users/*', {
        statusCode: 200,
        body: mockUser,
      }).as('getUserDetails');
  
      cy.visit('/my-account');
      cy.wait('@getUserDetails');
    });
  
    it('Loads user details successfully', () => {
      cy.get('.edit-user-form').within(() => {
        cy.get('input[name="username"]').should('have.value', mockUser.username);
        cy.get('input[name="email"]').should('have.value', mockUser.email);
        cy.get('input[name="firstName"]').should('have.value', mockUser.firstName);
        cy.get('input[name="lastName"]').should('have.value', mockUser.lastName);
      });
    });
  
    it('Validates required fields', () => {
      cy.get('input[name="username"]').clear();
      cy.get('input[name="email"]').clear();
      cy.get('input[name="firstName"]').clear();
      cy.get('input[name="lastName"]').clear();
      cy.get('.save-btn').click();
  
      cy.contains('Username is required').should('be.visible');
      cy.contains('Email is required').should('be.visible');
      cy.contains('First name is required').should('be.visible');
      cy.contains('Last name is required').should('be.visible');
    });
  
    it('Displays error if user details fail to load', () => {
      cy.intercept('GET', '**/users/*', { statusCode: 500 }).as('getUserError');
      cy.reload();
      cy.wait('@getUserError');
      cy.contains('Failed to load your personal information. Please try again.').should('be.visible');
    });
  
    it('Successfully saves updated user information', () => {
      const updatedUser = {
        ...mockUser,
        username: 'updateduser',
        email: 'updateduser@example.com',
        firstName: 'Updated',
        lastName: 'User',
      };
  
      cy.intercept('PUT', '**/users/*', {
        statusCode: 200,
      }).as('saveUser');
  
      cy.get('input[name="username"]').clear().type(updatedUser.username);
      cy.get('input[name="email"]').clear().type(updatedUser.email);
      cy.get('input[name="firstName"]').clear().type(updatedUser.firstName);
      cy.get('input[name="lastName"]').clear().type(updatedUser.lastName);
      cy.get('.save-btn').click();
  
      cy.wait('@saveUser');
      cy.contains('Your information has been updated successfully.').should('be.visible');
    });
  });  