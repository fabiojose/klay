package com.github.fabiojose.klay.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;

import com.github.fabiojose.klay.util.Compiler.CompilerException;

import org.junit.jupiter.api.Test;

public class CompilerTest {

  @Test
  void should_compile_and_create_instance_with_success() throws IOException {

    // setup
    var sourceFile = Path.of("src", "test", "resources", "Success.java");

    // act
    var instance = Compiler.getInstance().compileAndCreateInstance(sourceFile.toFile());

    // assert
    assertNotNull(instance);

  }

  @Test
  void should_fail_when_class_has_wrong_name() {

    // setup
    var sourceFile = Path.of("src", "test", "resources", "WrongClassName.java");

    // act
    var actual = assertThrows(CompilerException.class, () ->
      Compiler.getInstance().compileAndCreateInstance(sourceFile.toFile()));

    assertEquals("Wrong class name, must be t", actual.getMessage());
  }

  @Test
  void should_fail_when_not_implement_the_K_interface() {

    // setup
    var sourceFile = Path.of("src", "test", "resources", "NonImplementsKInterface.java");

    // act
    assertThrows(ClassCastException.class, () ->
      Compiler.getInstance().compileAndCreateInstance(sourceFile.toFile()));

  }
}
