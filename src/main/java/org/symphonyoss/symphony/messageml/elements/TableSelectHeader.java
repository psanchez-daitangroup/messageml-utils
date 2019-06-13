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
import org.symphonyoss.symphony.messageml.markdown.nodes.form.TableSelectHeaderNode;

import java.util.Collections;

/**
 * Class representing a table header container.
 *
 * @author Pedro Sanchez
 * @since 06/10/2019
 */
public class TableSelectHeader extends Element {
  public static final String MESSAGEML_TAG = "thead";

  public TableSelectHeader(Element parent) {
    super(parent, MESSAGEML_TAG);
  }

  @Override
  public String toString() {
    return "Header";
  }

  @Override
  public void validate() throws InvalidInputException {
    assertNoText();
    assertContentModel(Collections.<Class<? extends Element>>singleton(TableRow.class));
  }

  @Override
  public Node asMarkdown() {
    TableSelect tableSelect = (TableSelect) getParent();
    return new TableSelectHeaderNode(tableSelect.getPosition(), tableSelect.getHeaderText());
  }
}
