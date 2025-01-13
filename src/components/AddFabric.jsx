import React from 'react';
import { useForm } from 'react-hook-form';
import './AddFabric.css';

const AddFabric = ({ onAdd, onClose }) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = (data) => {
    const newFabric = {
      ...data,
      price: parseFloat(data.price),
      stock: parseInt(data.stock, 10),
      washable: data.washable || false,
      ironed: data.ironed || false,
    };

    onAdd(newFabric);
    onClose();
  };

  return (
    <div className="add-product-form" id="add-fabric-modal">
      <h3>Add Fabric</h3>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="form-group">
          {errors.name && <p className="error">{errors.name.message}</p>}
          <input 
            type="text"
            placeholder="Name"
            {...register('name', { required: 'Name is required' })}
          />
        </div>

        <div className="form-group">
          {errors.description && (
            <p className="error">{errors.description.message}</p>
          )}
          <textarea
            placeholder="Description"
            {...register('description', { required: 'Description is required' })}
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            {errors.material && <p className="error-row">{errors.material.message}</p>}
            <select id="material-select"
              {...register('material', { required: 'Please select a material' })}
            >
              <option value="">Select Material</option>
              <option value="COTTON">Cotton</option>
              <option value="POLYESTER">Polyester</option>
              <option value="SILK">Silk</option>
              <option value="WOOL">Wool</option>
              <option value="LINEN">Linen</option>
              <option value="LEATHER">Leather</option>
              <option value="DENIM">Denim</option>
              <option value="NYLON">Nylon</option>
              <option value="SATIN">Satin</option>
              <option value="VELVET">Velvet</option>
            </select>
          </div>

          <div className="form-group">
            {errors.color && <p className="error-row">{errors.color.message}</p>}
            <select id="color-select"
              {...register('color', { required: 'Please select a color' })}
            >
              <option value="">Select Color</option>
              <option value="RED">Red</option>
              <option value="BLUE">Blue</option>
              <option value="GREEN">Green</option>
              <option value="BLACK">Black</option>
              <option value="WHITE">White</option>
              <option value="YELLOW">Yellow</option>
              <option value="ORANGE">Orange</option>
              <option value="PURPLE">Purple</option>
              <option value="PINK">Pink</option>
              <option value="BROWN">Brown</option>
              <option value="GREY">Grey</option>
            </select>
          </div>

          <div className="form-group">
            {errors.price && <p className="error-row">{errors.price.message}</p>}
            <input
              type="number"
              step="0.01"
              placeholder="Price"
              {...register('price', {
                required: 'Price is required',
                validate: (value) =>
                  value > 0 || 'Price must be greater than 0',
              })}
            />
          </div>

          <div className="form-group">
            {errors.stock && <p className="error-row">{errors.stock.message}</p>}
            <input
              type="number"
              placeholder="Stock Quantity"
              {...register('stock', {
                required: 'Stock quantity is required',
                validate: (value) =>
                  value >= 0 ? true : 'Stock must be zero or greater',
              })}
            />
          </div>
        </div>
        
        <div className="form-group">
          {errors.pictureUrl && (
            <p className="error">{errors.pictureUrl.message}</p>
          )}
          <input
            type="text"
            placeholder="Picture URL (optional)"
            {...register('pictureUrl', {
              pattern: {
                value: /^(https?:\/\/.*\.(?:png|jpg|jpeg|svg|gif))?$/,
                message: 'Please enter a valid image URL',
              },
            })}
          />
        </div>

        <div className="checkbox-container">
          <label>
            <input
              type="checkbox"
              {...register('washable')}
            />
            Washable
          </label>

          <label>
            <input
              type="checkbox"
              {...register('ironed')}
            />
            Ironed
          </label>
        </div>

        <div className="button-row">
         <button type="submit" className="add-fabric-submit">Add</button>
          <button type="button" onClick={onClose}>Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default AddFabric;