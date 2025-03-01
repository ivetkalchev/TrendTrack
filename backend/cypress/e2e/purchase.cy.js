describe('Catalogue Purchase - Unauthenticated and Authenticated Flows', () => {
    const mockToken = 'mockAccessToken';
    const mockClaims = { userId: '12345', username: 'testuser' };
    const mockFabric1 = {
      id: 1,
      name: 'Silk Fabric',
      material: 'Silk',
      color: 'Red',
      price: 25.99,
      stock: 10,
      washable: true,
      ironed: true,
      description: 'Premium quality silk fabric.',
      pictureUrl: null,
    };
    const mockFabric2 = {
      id: 2,
      name: 'Cotton Fabric',
      material: 'Cotton',
      color: 'Blue',
      price: 15.99,
      stock: 15,
      washable: true,
      ironed: false,
      description: 'High-quality cotton fabric.',
      pictureUrl: null,
    };
  
    beforeEach(() => {
      cy.stub(window.localStorage, 'getItem')
        .withArgs('accessToken').returns(mockToken)
        .withArgs('claims').returns(JSON.stringify(mockClaims));
      cy.stub(window.localStorage, 'setItem').as('setItem');
      cy.stub(window.localStorage, 'removeItem').as('removeItem');
  
      cy.intercept('GET', '/fabrics*', {
        statusCode: 200,
        body: { fabrics: [mockFabric1, mockFabric2] },
      }).as('getFabrics');
  
      cy.intercept('POST', `/cart/${mockClaims.userId}/add`, {
        statusCode: 200,
        body: { success: true },
      }).as('addToCart');
  
      cy.window().then((win) => {
        win.localStorage.setItem('accessToken', mockToken);
        win.localStorage.setItem('claims', JSON.stringify(mockClaims));
      });
    });
  
    it('Trying to purchase without authentication', () => {
      cy.visit(`/catalogue`);
      cy.get('.product-list .product-admin-item').should('have.length', 2);
      cy.get('.cart-button').first().click();
      cy.on('window:alert', (text) => {
        expect(text).to.include('User is not authenticated.');
      });
    });
  
    it('Log in with mocked TokenManager', () => {
      cy.visit(`/authentication`);
  
      cy.intercept('POST', '/tokens/login', {
        statusCode: 200,
        body: { accessToken: mockToken },
      }).as('loginRequest');
  
      cy.get('input[placeholder="Enter your username"]').type('testuser');
      cy.get('input[placeholder="Enter your password"]').type('password123');
      cy.get('.submit-btn').click();
  
      cy.wait('@loginRequest');
      cy.window().then((win) => {
        expect(win.localStorage.getItem('accessToken')).to.eq(mockToken);
      });
  
      cy.url().should('include', '/catalogue');
    });
  
    it('Add mocked fabrics to the cart after login', () => {
      cy.visit(`/catalogue`);
  
      cy.wait('@getFabrics');
  
      cy.get('.product-list .product-admin-item')
        .should('have.length', 2)
        .and('contain.text', mockFabric1.name)
        .and('contain.text', mockFabric2.name);
  
      cy.get('.cart-button').first().click();
      cy.wait('@addToCart').its('request.body').should('deep.equal', {
        fabricId: mockFabric1.id,
        quantity: 1,
      });
  
      cy.get('.cart-button').eq(1).click();
      cy.wait('@addToCart').its('request.body').should('deep.equal', {
        fabricId: mockFabric2.id,
        quantity: 1,
      });
  
      cy.get('.added-message').should('be.visible').and('contain.text', 'Added to cart!');
    });
  
    it('View the cart, remove one fabric, and proceed with an order', () => {
        const mockCart = {
          user: { id: mockClaims.userId, username: mockClaims.username },
          items: [
            {
              fabric: mockFabric1,
              quantity: 1,
              totalPrice: mockFabric1.price,
            },
            {
              fabric: mockFabric2,
              quantity: 1,
              totalPrice: mockFabric2.price,
            },
          ],
          totalCost: mockFabric1.price + mockFabric2.price,
        };
    
        cy.intercept('GET', `/cart/${mockClaims.userId}`, (req) => {
          req.reply({ statusCode: 200, body: mockCart });
        }).as('getCart');
    
        cy.intercept('PUT', `/cart/${mockClaims.userId}/update`, {
          statusCode: 200,
          body: mockCart,
        }).as('updateCart');
    
        cy.intercept('DELETE', `/cart/${mockClaims.userId}/remove*`, (req) => {
          const fabricId = parseInt(req.query.fabricId, 10);
    
          if (req.headers.authorization === `Bearer ${mockToken}`) {
            mockCart.items = mockCart.items.filter((item) => item.fabric.id !== fabricId);
            mockCart.totalCost = mockCart.items.reduce((sum, item) => sum + item.totalPrice, 0);
    
            req.reply({ statusCode: 200, body: { success: true } });
          } else {
            req.reply({ statusCode: 401, body: { error: 'Unauthorized' } });
          }
        }).as('removeFromCart');
    
        cy.intercept('POST', '/orders', {
          statusCode: 200,
          body: { success: true },
        }).as('createOrder');
    
        cy.visit(`/cart`);
        cy.wait('@getCart');
        cy.get('.cart-item').should('have.length', 2);
    
        cy.get('.cart-item-remove-button').first().click({ force: true });
        cy.wait('@removeFromCart').then(() => {
          cy.wait('@getCart');
        });
    
        cy.get('.cart-item').should('have.length', 1);
        cy.get('.cart-item-name').should('contain.text', mockFabric2.name);
    
        cy.get('#address').type('123 Fashion St, Style City');
        cy.get('.proceed-order-button').click();
        cy.wait('@createOrder').then((interception) => {
            cy.log(JSON.stringify(interception.request.body, null, 2));
        });        
    
        cy.get('.empty-cart-message').should('contain.text', 'Your cart is empty!');
    });    

    it('View user orders on the /my-orders page', () => {
        const mockOrders = [
            {
                id: 'ORD123',
                items: [
                    {
                        fabric: mockFabric1,
                        quantity: 1,
                        pricePerUnit: mockFabric1.price,
                        totalPrice: mockFabric1.price,
                    },
                    {
                        fabric: mockFabric2,
                        quantity: 1,
                        pricePerUnit: mockFabric2.price,
                        totalPrice: mockFabric2.price,
                    },
                ],
                totalAmount: mockFabric1.price + mockFabric2.price,
                status: 'Delivered',
                orderDate: '2025-01-10T10:30:00Z',
            },
        ];
    
        cy.intercept('GET', `/orders/users/${mockClaims.userId}`, {
            statusCode: 200,
            body: { orders: mockOrders },
        }).as('getUserOrders');
    
        cy.visit(`/my-orders`);
        cy.wait('@getUserOrders');
    
        cy.get('h1').should('contain.text', 'Your Orders');
        cy.get('table.order-table').should('exist');
        cy.get('tbody tr').should('have.length', 1);
    
        cy.get('tbody tr').eq(0).within(() => {
            cy.get('td').eq(0).should('contain.text', 'ORD123');
            cy.get('td').eq(1).should('contain.text', 'Silk Fabric');
            cy.get('td').eq(1).should('contain.text', 'Cotton Fabric');
            cy.get('td').eq(2).should('contain.text', `€${mockOrders[0].totalAmount.toFixed(2)}`);
            cy.get('td').eq(3).should('contain.text', 'Delivered');
    
            const expectedDate = new Date(mockOrders[0].orderDate).toLocaleDateString(); 
            cy.get('td').eq(4).should('contain.text', expectedDate);
        });
    });    
  });  