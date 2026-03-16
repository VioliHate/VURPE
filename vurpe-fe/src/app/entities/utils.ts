import { QueryNode, QueryGroup, QueryRule } from './types';

export function generateSQL(node: QueryNode): string {
  if (node.type === 'rule') {
    return formatRule(node);
  }

  if (node.children.length === 0) return '';
  
  const childrenSQL = node.children
    .map(child => generateSQL(child))
    .filter(sql => sql !== '')
    .join(` ${node.logicalOperator} `);

  return node.children.length > 1 ? `(${childrenSQL})` : childrenSQL;
}

function formatRule(rule: QueryRule): string {
  const { field, operator, value, value2 } = rule;

  switch (operator) {
    case 'IS NULL':
    case 'IS NOT NULL':
      return `${field} ${operator}`;
    case 'BETWEEN':
      return `${field} BETWEEN ${value} AND ${value2}`;
    case 'LIKE':
      return `${field} LIKE '%${value}%'`;
    case 'LENGTH <':
      return `length(${field}) < ${value}`;
    case 'LENGTH >':
      return `length(${field}) > ${value}`;
    default:
      // Handle string quotes if field is category or description
      const formattedValue = (field === 'category' || field === 'description') 
        ? `'${value}'` 
        : value;
      return `${field} ${operator} ${formattedValue}`;
  }
}

export function createEmptyGroup(): QueryGroup {
  return {
    id: crypto.randomUUID(),
    type: 'group',
    logicalOperator: 'AND',
    children: []
  };
}

export function createEmptyRule(): QueryRule {
  return {
    id: crypto.randomUUID(),
    type: 'rule',
    field: 'amount',
    operator: '=',
    value: ''
  };
}
