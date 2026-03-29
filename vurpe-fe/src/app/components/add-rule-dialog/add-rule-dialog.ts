import { Component, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { RiskFlag, Field, FieldType, Operator, RuleGroup, Condition } from '../../data/types';
import { BusinessRule } from '../../data/BusinessRule';
import { MatTooltip } from '@angular/material/tooltip';

@Component({
  selector: 'app-add-rule-dialog',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    CommonModule,
    FormsModule,
    MatIconModule,
    MatTooltip,
  ],
  templateUrl: './add-rule-dialog.html',
  styleUrl: './add-rule-dialog.scss',
})
export class AddRuleDialog {
  [x: string]: any;
  private dialogRef = inject(MatDialogRef<AddRuleDialog>);

  ruleName = signal('New Business Rule');
  riskFlag = signal<RiskFlag>(RiskFlag.LOW);
  severity = signal(5);

  readonly flags = Object.values(RiskFlag) as RiskFlag[];

  fields: Field[] = [
    { id: 'amount', label: 'Importo', type: 'number' },
    { id: 'category', label: 'Categoria', type: 'string' },
    { id: 'date', label: 'Data', type: 'date' },
    { id: 'description', label: 'Descrizione', type: 'string' },
  ];

  operatorsByType: Record<FieldType, { label: string; value: Operator }[]> = {
    string: [
      { label: 'Equals', value: 'equals' },
      { label: 'Not Equals', value: 'not_equals' },
      { label: 'Contains', value: 'contains' },
      { label: 'Starts With', value: 'starts_with' },
      { label: 'Ends With', value: 'ends_with' },
      { label: 'Is Empty', value: 'is_null' },
      { label: 'Is Not Empty', value: 'is_not_null' },
    ],
    number: [
      { label: '=', value: 'equals' },
      { label: '!=', value: 'not_equals' },
      { label: '>', value: 'gt' },
      { label: '<', value: 'lt' },
      { label: '>=', value: 'gte' },
      { label: '<=', value: 'lte' },
    ],
    date: [
      { label: 'On', value: 'on' },
      { label: 'Before', value: 'before' },
      { label: 'After', value: 'after' },
      { label: 'Is Empty', value: 'is_null' },
    ],
  };

  rule: RuleGroup = {
    id: 'root',
    conjunction: 'AND',
    conditions: [{ id: '1', fieldId: 'category', operator: 'equals', value: 'FINANCE' }],
  };

  getOperators(fieldId: string) {
    const field = this.fields.find((f) => f.id === fieldId);
    return field ? this.operatorsByType[field.type] : [];
  }

  getFieldType(fieldId: string): string {
    const field = this.fields.find((f) => f.id === fieldId);
    if (!field) return 'text';
    if (field.type === 'number') return 'number';
    if (field.type === 'date') return 'date';
    return 'text';
  }

  addCondition(group: RuleGroup) {
    group.conditions.push({
      id: Math.random().toString(36).substring(2, 9),
      fieldId: this.fields[0].id,
      operator: 'equals',
      value: '',
    });
  }

  addGroup(group: RuleGroup) {
    group.conditions.push({
      id: Math.random().toString(36).substring(2, 9),
      conjunction: 'AND',
      conditions: [
        {
          id: Math.random().toString(36).substring(2, 9),
          fieldId: this.fields[0].id,
          operator: 'equals',
          value: '',
        },
      ],
    });
  }

  removeCondition(parentGroup: RuleGroup, id: string) {
    parentGroup.conditions = parentGroup.conditions.filter((c) => c.id !== id);
  }

  toggleConjunction(group: RuleGroup) {
    group.conjunction = group.conjunction === 'AND' ? 'OR' : 'AND';
  }

  isGroup(item: Condition | RuleGroup): item is RuleGroup {
    return 'conjunction' in item;
  }

  get ruleString(): string {
    const stringify = (g: RuleGroup): string => {
      const parts = g.conditions.map((c) => {
        if (this.isGroup(c)) return `(${stringify(c)})`;
        const field = this.fields.find((f) => f.id === c.fieldId)?.id;
        const op = c.operator.replace(/_/g, ' ').toUpperCase();
        return `${field} ${op} '${c.value}'`;
      });
      return parts.join(` ${g.conjunction} `);
    };
    return stringify(this.rule);
  }

  saveRule() {
    let buildedRule = new BusinessRule(
      this.ruleName(),
      this.ruleString,
      this.riskFlag(),
      this.severity(),
    );
    this.dialogRef.close(buildedRule);
  }

  close() {
    this.dialogRef.close();
  }
}
