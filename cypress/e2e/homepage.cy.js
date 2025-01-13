describe('Homepage Test', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('Loads the homepage successfully', () => {
    cy.get('.home-container').should('exist');
  });

  it('Displays hero section content correctly', () => {
    cy.get('.hero-section h1').should('contain.text', 'A Vibrant Form of Self Expression');
    cy.get('.hero-section p').should('contain.text', '(click)');
    cy.get('.scroll-icon img').should('have.attr', 'alt', 'Scroll Icon');
  });

  it('Scrolls to About Us section when scroll icon is clicked', () => {
    cy.get('.scroll-icon').click();
    cy.url().should('include', '#about');
    cy.get('#about').should('be.visible');
  });

  it('Displays About Us section content correctly', () => {
    cy.get('#about h2').should('contain.text', 'About Us');
    cy.get('.about-content .about-text').should('exist');
    cy.get('.about-content .about-text p').eq(0)
      .should('contain.text', 'At TrendTrack, we believe that fashion isn’t just about what you wear—it’s a vibrant form of self-expression!');
    cy.get('.about-content .about-image img').should('have.attr', 'alt', 'Fabric');
  });

  it('Redirects to catalogue page on Browse Catalogue button click', () => {
    cy.get('.browse-button').click();
    cy.url().should('include', '/catalogue');
  });
});