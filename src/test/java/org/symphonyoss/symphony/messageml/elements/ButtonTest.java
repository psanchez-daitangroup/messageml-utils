package org.symphonyoss.symphony.messageml.elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.symphonyoss.symphony.messageml.exceptions.InvalidInputException;

public class ButtonTest extends ElementTest{

  @Test
  public void testCompleteButton() throws Exception {
    String type = "action";
    String name = "action-btn-name";
    String clazz = "primary";
    String innerText = "Complete";
    String input = "<messageML><form><button type=\"" + type + "\" class=\"" + clazz + "\" name=\"" + name + "\">"
            + innerText + "</button></form></messageML>";
    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);

    Element messageML = context.getMessageML();
    Element form = messageML.getChildren().get(0);
    Element button = form.getChildren().get(0);

    assertEquals("Button class", Button.class, button.getClass());
    verifyButtonPresentation((Button) button, name, type, clazz, innerText);
  }

  @Test
  public void testResetButton() throws Exception {
    String type = "reset";
    String innerText = "Reset";
    String input = "<messageML><form><button type=\"" + type + "\">" + innerText + "</button></form></messageML>";
    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);

    Element messageML = context.getMessageML();
    Element form = messageML.getChildren().get(0);
    Element button = form.getChildren().get(0);

    assertEquals("Button class", Button.class, button.getClass());
    verifyButtonPresentation((Button) button,null, type, null, innerText);
  }

  @Test
  public void testTypelessButtonWithName() throws Exception {
    String innerText = "Typeless Button With Name";
    String name = "btn-name";
    String input = "<messageML><form><button name=\"" + name + "\">" + innerText + "</button></form></messageML>";

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);

    Element messageML = context.getMessageML();
    Element form = messageML.getChildren().get(0);
    Element button = form.getChildren().get(0);

    assertEquals("Button class", Button.class, button.getClass());
    verifyButtonPresentation((Button) button, name, "action", null, innerText);
  }

  @Test
  public void testActionButtonWithName() throws Exception {
    String type = "action";
    String name = "btn-name";
    String innerText = "Action Button With Name";
    String input = "<messageML><form><button type=\"" + type + "\" name=\"" + name + "\">" + innerText
            + "</button></form></messageML>";

    context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);

    Element messageML = context.getMessageML();
    Element form = messageML.getChildren().get(0);
    Element button = form.getChildren().get(0);

    assertEquals("Button class", Button.class, button.getClass());
    verifyButtonPresentation((Button) button, name, type, null, innerText);
  }

  @Test
  public void testButtonWithValidClasses() throws Exception {
    String type = "reset";
    String innerText = "Class Test";

    for (String clazz : Button.VALID_CLASSES) {
      String input = "<messageML><form><button type=\"" + type + "\" class=\"" + clazz + "\">" + innerText
              + "</button></form></messageML>";
      context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);

      Element messageML = context.getMessageML();
      Element form = messageML.getChildren().get(0);
      Element button = form.getChildren().get(0);

      assertEquals("Button class", Button.class, button.getClass());
      verifyButtonPresentation((Button) button, null, type, clazz, innerText);
    }
  }

  @Test
  public void testTypelessButtonWithoutName() throws Exception {
    String innerText = "Typeless Button Without Name";
    String input = "<messageML><form><button>" + innerText + "</button></form></messageML>";

    try {
      context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
      fail("Should have thrown an exception on typeless button without name");
    } catch (Exception e) {
      assertEquals("Exception class", InvalidInputException.class, e.getClass());
      assertEquals("Exception message", "Attribute \"name\" is required for generic action buttons", e.getMessage());
    }
  }

  @Test
  public void testActionButtonWithoutName() throws Exception {
    String type = "action";
    String innerText = "Typeless Button Without Name";
    String input = "<messageML><form><button type=\"" + type + "\">" + innerText + "</button></form></messageML>";

    try {
      context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
      fail("Should have thrown an exception on action button without name");
    } catch (Exception e) {
      assertEquals("Exception class", InvalidInputException.class, e.getClass());
      assertEquals("Exception message", "Attribute \"name\" is required for generic action buttons", e.getMessage());
    }
  }

  @Test
  public void testMisplacedButton() throws Exception {
    String innerText = "Misplaced Button";
    String type = "reset";
    String input = "<messageML><button type=\"" + type + "\">" + innerText + "</button></messageML>";

    try {
      context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
      fail("Should have thrown an exception on button out of a form tag");
    } catch (Exception e) {
      assertEquals("Exception class", InvalidInputException.class, e.getClass());
      assertEquals("Exception message", "Element \"button\" can only be a child of the following elements: \"form\"", e.getMessage());
    }
  }

  @Test
  public void testBadTypeButton() throws Exception {
    String innerText = "Invalid Type Button";
    String type = "potato";
    String input = "<messageML><form><button type=\"" + type + "\">" + innerText + "</button></form></messageML>";

    try {
      context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
      fail("Should have thrown an exception on invalid type button");
    } catch (Exception e) {
      assertEquals("Exception class", InvalidInputException.class, e.getClass());
      assertEquals("Exception message", "Attribute \"type\" must be \"action\" or \"reset\"", e.getMessage());
    }
  }

  @Test
  public void testBadClassButton() throws Exception {
    String innerText = "Invalid Class Button";
    String type = "reset";
    String clazz = "outclassed";
    String input = "<messageML><form><button type=\"" + type + "\" class=\"" + clazz + "\">" + innerText
            + "</button></form></messageML>";

    try {
      context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
      fail("Should have thrown an exception on invalid class button");
    } catch (Exception e) {
      assertEquals("Exception class", InvalidInputException.class, e.getClass());
      assertEquals("Exception message", "Attribute \"class\" must be \"primary\", \"secondary\", " +
              "\"primary-destructive\" or \"secondary-destructive\"", e.getMessage());
    }
  }

  @Test
  public void testBadAttributeButton() throws Exception {
    String innerText = "Invalid Attribute Button";
    String type = "reset";
    String invalidAttribute = "invalid-attribute";
    String input = "<messageML><form><button type=\"" + type + "\" " + invalidAttribute + "=\"invalid\">" + innerText
            + "</button></form></messageML>";

    try {
      context.parseMessageML(input, null, MessageML.MESSAGEML_VERSION);
      fail("Should have thrown an exception on invalid attribute of button");
    } catch (Exception e) {
      assertEquals("Exception class", InvalidInputException.class, e.getClass());
      assertEquals("Exception message", "Attribute \"" + invalidAttribute + "\" is not allowed in \""
              + Button.MESSAGEML_TAG + "\"", e.getMessage());
    }
  }

  private String getNamePresentationML(String name) {
    if (name != null) {
      return " name=\"" + name + "\"";
    } else {
      return "";
    }
  }

  private String getClassPresentationML(String clazz) {
    if (clazz != null) {
      return " class=\"" + clazz + "\"";
    } else {
      return "";
    }
  }

  private String getExpectedButtonPresentation(String name, String type, String clazz, String innerText) {
    return "<div data-format=\"PresentationML\" data-version=\"2.0\"><form><button type=\"" + type + "\""
            + getClassPresentationML(clazz) + getNamePresentationML(name) + ">" + innerText + "</button></form></div>";
  }

  private String getExpectedButtonMarkdown(String innerText) {
    return "Form (log into desktop client to answer):\n---\n(Button:"+ innerText + ")\n---\n";
  }

  private void verifyButtonPresentation(Button button, String name, String type, String clazz, String innerText) {
    assertEquals("Button name attribute", name, button.getAttribute(Button.NAME_ATTR));
    assertEquals("Button type attribute", type, button.getAttribute(Button.TYPE_ATTR));
    assertEquals("Button clazz attribute", clazz, button.getAttribute(Button.CLASS_ATTR));
    assertEquals("Button inner text", innerText, button.getChild(0).asText());

    assertEquals("Button markdown", getExpectedButtonMarkdown(innerText), context.getMarkdown());
    assertEquals("Button presentationML", getExpectedButtonPresentation(name, type, clazz, innerText),
            context.getPresentationML());
  }
}
