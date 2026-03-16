import { Component, computed, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButton } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';

import { 
  QueryGroup, 
  QueryRule, 
  QueryNode, 
  FIELDS, 
  OPERATORS, 
  RiskFlag, 
  BusinessRule 
} from '../../entities/types';
import { generateSQL, createEmptyGroup, createEmptyRule } from   '../../entities/utils';


@Component({
  selector: 'app-add-rule-dialog',
  imports: [
       MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatButton,CommonModule, FormsModule, MatIconModule
    
  ],
  templateUrl: './add-rule-dialog.html',
  styleUrl: './add-rule-dialog.scss',
})
export class AddRuleDialog {
  private dialogRef = inject(MatDialogRef<AddRuleDialog>);
   ruleName = signal('New Business Rule');
  riskFlag = signal<RiskFlag>('LOW');
  severity = signal(5);
  query = signal<QueryGroup>(createEmptyGroup());
  savedRules = signal<BusinessRule[]>([]);

  generatedSQL = computed(() => generateSQL(this.query()));

  FIELDS = FIELDS;
  OPERATORS = Object.values(OPERATORS);
  RISK_FLAGS: RiskFlag[] = ['LOW', 'MEDIUM', 'HIGH'];
  createEmptyGroup = createEmptyGroup;

  resetQuery() {
    this.query.set(createEmptyGroup());
  }

  onNoClick() {
    // For now, let's just reset the form or something similar
    this.ruleName.set('New Business Rule');
    this.riskFlag.set('LOW');
    this.severity.set(5);
    this.resetQuery();
  }

  addRule(groupId: string) {
    this.updateQuery(groupId, (group) => ({
      ...group,
      children: [...group.children, createEmptyRule()]
    }));
  }

  addGroup(groupId: string) {
    this.updateQuery(groupId, (group) => ({
      ...group,
      children: [...group.children, createEmptyGroup()]
    }));
  }

  removeNode(id: string) {
    const filterGroup = (group: QueryGroup): QueryGroup => ({
      ...group,
      children: group.children
        .filter(child => child.id !== id)
        .map(child => child.type === 'group' ? filterGroup(child) : child)
    });
    this.query.set(filterGroup(this.query()));
  }

  updateRule(id: string, updates: Partial<QueryRule>) {
    const updateInGroup = (group: QueryGroup): QueryGroup => ({
      ...group,
      children: group.children.map(child => {
        if (child.id === id) return { ...child, ...updates } as QueryRule;
        if (child.type === 'group') return updateInGroup(child);
        return child;
      })
    });
    this.query.set(updateInGroup(this.query()));
  }

  updateGroupOperator(id: string, logicalOperator: 'AND' | 'OR') {
    const updateInGroup = (group: QueryGroup): QueryGroup => {
      if (group.id === id) return { ...group, logicalOperator };
      return {
        ...group,
        children: group.children.map(child => 
          child.type === 'group' ? updateInGroup(child) : child
        )
      };
    };
    this.query.set(updateInGroup(this.query()));
  }

  private updateQuery(groupId: string, updater: (group: QueryGroup) => QueryGroup) {
    const updateRecursive = (group: QueryGroup): QueryGroup => {
      if (group.id === groupId) return updater(group);
      return {
        ...group,
        children: group.children.map(child => 
          child.type === 'group' ? updateRecursive(child) : child
        )
      };
    };
    this.query.set(updateRecursive(this.query()));
  }

  handleSave() {
    const newRule: BusinessRule = {
      id: crypto.randomUUID(),
      ruleName: this.ruleName(),
      ruleCondition: this.generatedSQL(),
      riskFlag: this.riskFlag(),
      severity: this.severity()
    };
    this.savedRules.update(rules => [newRule, ...rules]);
  }

  getOpConfig(operator: string) {
    return Object.values(OPERATORS).find(o => o.value === operator);
  }
}
