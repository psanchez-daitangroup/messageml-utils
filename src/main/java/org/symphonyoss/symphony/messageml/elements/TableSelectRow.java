/*
 * Copyright 2016-2017 MessageML - Symphony LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.symphonyoss.symphony.messageml.elements;

import org.commonmark.node.Node;
import org.symphonyoss.symphony.messageml.exceptions.InvalidInputException;
import org.symphonyoss.symphony.messageml.markdown.nodes.form.TableSelectRowNode;
import org.symphonyoss.symphony.messageml.util.XmlPrintStream;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class representing a table row container.
 *
 * @author Pedro Sanchez
 * @since 06/10/2019
 */
public class TableSelectRow extends Element {
  public static final String MESSAGEML_TAG = "tr";

  private static final String PRESENTATIONML_TABLE_ROW_TAG = "tr";
  private static final String PRESENTATIONML_TABLE_CELL_TAG = "td";
  private static final String PRESENTATIONML_INPUT_TAG = "input";
  private static final String PRESENTATIONML_BUTTON_TAG = "button";

  private final static String HEADER_SUFIX = "header";
  private final static String ROW_SUFIX = "row";


  private static final String PRESENTATIONML_TYPE_ATTR = "type";
  private static final String PRESENTATIONML_NAME_ATTR = "name";
  private static final String PRESENTATIONML_INPUT_TYPE = "checkbox";

  private final static String LEFT = "left";
  private final static String RIGHT = "right";
  private final static String BUTTON = "button";
  private final static String CHECKBOX = "checkbox";

  private final Long rowNumber;

  private final TableSelect tableSelect;

  public TableSelectRow(Element parent, Long rowNumber, TableSelect tableSelect) {
    super(parent, MESSAGEML_TAG);
    this.rowNumber = rowNumber;
    this.tableSelect = tableSelect;
  }

  @Override
  public String toString() {
    return "TableSelect Row";
  }

  @Override
  public void validate() throws InvalidInputException {
    assertNoText();
    assertContentModel(Arrays.asList(TableHeaderCell.class, TableCell.class));
  }

  @Override
  public Node asMarkdown() {
    return new TableSelectRowNode(tableSelect.getPosition(), tableSelect.getType(), tableSelect.getButtonText());
  }

  @Override
  public void asPresentationML(XmlPrintStream out) {
    out.openElement(PRESENTATIONML_TABLE_ROW_TAG);

    if (TableHeader.class.equals(getParent().getClass())) {
      buildPresentationMLTableSelectRow(out, HEADER_SUFIX);
    }

    if (TableBody.class.equals(getParent().getClass())) {
      buildPresentationMLTableSelectRow(out, ROW_SUFIX + rowNumber);
    }

    out.closeElement(); // Closing table
  }


  private void buildPresentationMLTableSelectRow(XmlPrintStream out, String sufix) {
    if (isLeftPositioned()) {
      buildPresentationMLTableSelectType(out, sufix);
    }

    for (Element child : getChildren()) {
      child.asPresentationML(out);
    }

    if (isRightPositioned()) {
      buildPresentationMLTableSelectType(out, sufix);
    }
  }

  private void buildPresentationMLTableSelectType(XmlPrintStream out, String sufix) {
    out.openElement(PRESENTATIONML_TABLE_CELL_TAG);

    StringBuilder name = new StringBuilder(tableSelect.getName())
        .append("-")
        .append(sufix);

    if (isCheckboxType()) {
      buildPresentationMLCheckBox(out, name.toString());
    } else if (isButtonType()) {
      if (HEADER_SUFIX.equals(sufix)) {
        out.print(tableSelect.getHeaderText());
      } else {
        buildPresentationMLButton(out, name.toString());
      }
    }

    out.closeElement();
  }

  private void buildPresentationMLCheckBox(XmlPrintStream out, String name) {
    Map<String, String> presentationAttrs = new LinkedHashMap<>();
    presentationAttrs.put(PRESENTATIONML_TYPE_ATTR, PRESENTATIONML_INPUT_TYPE);
    presentationAttrs.put(PRESENTATIONML_NAME_ATTR, name);

    out.printElement(PRESENTATIONML_INPUT_TAG, presentationAttrs);
  }

  private void buildPresentationMLButton(XmlPrintStream out, String name) {
    Map<String, String> presentationAttrs = new LinkedHashMap<>();
    presentationAttrs.put(PRESENTATIONML_NAME_ATTR, name);

    out.printElement(PRESENTATIONML_BUTTON_TAG, presentationAttrs);
  }

  private Boolean isRightPositioned() {
    return RIGHT.equals(tableSelect.getPosition());
  }

  private Boolean isLeftPositioned() {
    return LEFT.equals(tableSelect.getPosition());
  }

  private Boolean isButtonType() {
    return BUTTON.equals(tableSelect.getType());
  }

  private Boolean isCheckboxType() {
    return CHECKBOX.equals(tableSelect.getType());
  }
}