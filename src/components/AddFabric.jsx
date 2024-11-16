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
    <div className="add-product-form">
      <h3>Add Fabric</h3>
      <form onSubmit={handleSubmit(onSubmit)}>
        <input
          type="text"
          placeholder="Name"
          {...register('name', { required: 'Name is required' })}
        />
        {errors.name && <p className="error">{errors.name.message}</p>}

        <textarea
          placeholder="Description"
          {...register('description', { required: 'Description is required' })}
        />
        {errors.description && <p className="error">{errors.description.message}</p>}

        <div className="form-row">
          <select
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
          {errors.material && <p className="error">{errors.material.message}</p>}

          <select
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
          {errors.color && <p className="error">{errors.color.message}</p>}
        </div>

        <div className="form-row">
          <input
            type="number"
            placeholder="Price"
            {...register('price', {
              required: 'Price is required',
              validate: (value) =>
                value > 0 ? true : 'Price must be greater than 0',
            })}
          />
          {errors.price && <p className="error">{errors.price.message}</p>}

          <input
            type="number"
            placeholder="Stock Quantity"
            {...register('stock', {
              required: 'Stock quantity is required',
              validate: (value) =>
                value >= 0 ? true : 'Stock must be zero or greater',
            })}
          />
          {errors.stock && <p className="error">{errors.stock.message}</p>}
        </div>

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
        {errors.pictureUrl && <p className="error">{errors.pictureUrl.message}</p>}

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
          <button type="submit">Add</button>
          <button type="button" onClick={onClose}>Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default AddFabric;