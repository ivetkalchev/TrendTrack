describe('Authentication Flow', () => {
    beforeEach(() => {
      cy.visit('/authentication');
    });
  
    it('Displays login form by default', () => {
      cy.get('form').within(() => {
        cy.contains('Welcome Back').should('be.visible');
        cy.get('input[placeholder="Enter your username"]').should('exist');
        cy.get('input[placeholder="Enter your password"]').should('exist');
      });
      cy.contains('Register').should('be.visible');
    });
  
    it('Validates login form fields', () => {
      cy.get('form').within(() => {
        cy.get('button[type="submit"]').click();
      });
      cy.contains('Username is required').should('be.visible');
      cy.contains('Password is required').should('be.visible');
    });
  
    it('Handles incorrect login credentials', () => {
      cy.get('input[placeholder="Enter your username"]').type('invalidUser');
      cy.get('input[placeholder="Enter your password"]').type('wrongPassword');
      cy.get('button[type="submit"]').click();
      cy.on('window:alert', (text) => {
        expect(text).to.equal('Login Failed. Please try again.');
      });
    });
  
    it('Logs in with correct credentials', () => {
        cy.intercept('POST', '/tokens/login', {
          statusCode: 200,
          body: { accessToken: 'mockedAccessToken' },
        }).as('loginRequest');
      
        cy.get('input[placeholder="Enter your username"]').type('validUser');
        cy.get('input[placeholder="Enter your password"]').type('validPassword');
        cy.get('button[type="submit"]').click();
      
        cy.wait('@loginRequest');
        cy.url().should('include', '/catalogue');
      
        cy.contains('Catalogue').should('be.visible');
      });  
  
    it('Switches to registration form', () => {
      cy.contains('Register').click();
      cy.contains('Join Our Family').should('be.visible');
      cy.get('input[placeholder="Enter your username"]').should('exist');
      cy.get('input[placeholder="Enter your email"]').should('exist');
      cy.get('input[placeholder="Enter your first name"]').should('exist');
      cy.get('input[placeholder="Enter your last name"]').should('exist');
      cy.get('input[placeholder="Enter your password"]').should('exist');
    });
  
    it('Validates registration form fields', () => {
      cy.contains('Register').click();
      cy.get('form').within(() => {
        cy.get('button[type="submit"]').click();
      });
      cy.contains('Username is required').should('be.visible');
      cy.contains('Password is required').should('be.visible');
      cy.contains('Email is required').should('be.visible');
      cy.contains('First name is required').should('be.visible');
      cy.contains('Last name is required').should('be.visible');
    });
  
    it('Registers successfully with valid inputs', () => {
        cy.intercept('POST', '/users', {
          statusCode: 201,
          body: { message: 'User registered successfully' },
        }).as('registerRequest');
      
        cy.contains('Register').click();
        cy.get('input[placeholder="Enter your username"]').type('uniqueUser');
        cy.get('input[placeholder="Enter your email"]').type('uniqueuser@example.com');
        cy.get('input[placeholder="Enter your first name"]').type('Unique');
        cy.get('input[placeholder="Enter your last name"]').type('User');
        cy.get('input[placeholder="Enter your password"]').type('StrongPass123');
        cy.get('button[type="submit"]').click();
      
        cy.wait('@registerRequest');
        cy.on('window:alert', (text) => {
          expect(text).to.equal('Registration Successful!');
        });
      });      
  });
  