import React from 'react';
import moment from 'moment';

export const sizePerPageRenderer = ({
  options,
  currSizePerPage,
  onSizePerPageChange
}) => (
  <div className="btn-group" role="group">
    {
      options.map((option) => {
        const isSelect = currSizePerPage === `${option.page}`;
        return (
          <button
            key={ option.text }
            type="button"
            onClick={ () => onSizePerPageChange(option.page) }
            className={ `btn ${isSelect ? 'btn-primary' : 'btn-light'}` }
          >
            { option.text }
          </button>
        );
      })
    }
  </div>
);

export const first_host = "https://localhost:50432/lab2/api/labworks"
export const second_host = "https://localhost:8181/lab2/bars/disciplines"
export const third_host = "https://localhost:8181/lab2/bars/labworks"


export const options = {
  'VERY_EASY': 'VERY_EASY',
  'EASY': 'EASY',
  'INSANE': 'INSANE',
  'HOPELESS': 'HOPELESS',
  'TERRIBLE': 'TERRIBLE'
};

export const defaultSorted = [{
  dataField: 'id',
  order: 'desc'
}];

export function constructObjectLab(params){
    return {
        labWork: {
            name: params.name,
            minimalPoint: params.minimalPoint,
            maximumPoint: params.maximumPoint,
            personalQualitiesMaximum: params.personalQualitiesMaximum,
            difficulty: params.difficulty,
            coordinates: {
              x: params.coordinatesX,
              y: params.coordinatesY
            },
            author: {
              name: params.authorName,
              weight: params.authorWeight,
              location: {
                x: params.locationX,
                y: params.locationY,
                z: params.locationZ,
                name: params.locationName
              }
            },
          discipline: {
              id: params.discipline
          }
        }
    }
}

export function constructObjectDiscipline(params){
  return {
    discipline: {
      name: params.name
    }
  }
}

export function constructQueryParams(filters, sizePerPage, page, sortField){
  const params = {
    name: filters.name,
    creationDate: filters.creationDate,
    minimalPoint: filters.minimalPoint,
    maximumPoint: filters.maximumPoint,
    personalQualitiesMaximum: filters.personalQualitiesMaximum,
    difficulty: filters.difficulty,
    coordinatesX: filters["coordinates.x"],
    coordinatesY: filters["coordinates.y"],
    authorName: filters["author.name"],
    authorWeight: filters["author.weight"],
    locationX: filters["author.location.x"],
    locationY: filters["author.location.y"],
    locationZ: filters["author.location.z"],
    locationName: filters["author.location.name"],
    pageIdx: page,
    pageSize: sizePerPage,
    sortField: sortField
  };
  for (const key of Object.keys(params)) {
    if (params[key] === "") {
      delete params[key];
    }
  }
  return params
}

export function constructSortField(field){
  switch (field){
    case "coordinates.x":
      return "coordinatesX";
    case "coordinates.y":
      return "coordinatesY";
    case "author.name":
      return "authorName"
    case "author.weight":
      return "authorWeight";
    case "author.location.x":
      return "locationX"
    case "author.location.y":
      return "locationY"
    case "author.location.z":
      return "locationZ"
    case "author.location.name":
      return "locationName"
    default:
      return field
  }
}

export function constructUpdateObject(object, field, newValue){
  switch (field){
    case "creationDate":
      object.creationDate = moment(newValue).format('DD.MM.yyyy')
      break
    case "coordinates.x":
      object.coordinates.x = newValue;
      break
    case "coordinates.y":
      object.coordinates.y = newValue;
      break;
    case "author.name":
      object.author.name = newValue
      break
    case "author.weight":
      object.author.weight = newValue
      break;
    case "author.location.x":
      object.author.location.x = newValue
      break
    case "author.location.y":
      object.author.location.y = newValue
      break
    case "author.location.z":
      object.author.location.z = newValue
      break
    case "author.location.name":
      object.author.location.name = newValue
      break
    default:
      object[field] = newValue
  }
}