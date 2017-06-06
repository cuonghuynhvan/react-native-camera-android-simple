// using JS option for this file, so we can comment it

module.exports = {
  "parser": "babel-eslint",
  "env": {
    "browser": true,
  },
  "extends": "airbnb",
  "plugins": [
    "react",
    "jsx-a11y",
    "import",
    "jest",
  ],
  rules: {
    // overrides
    "react/require-default-props": 0,
    "react/jsx-filename-extension": 0,
    "react/no-string-refs": 0,
    "react/forbid-prop-types": 2,
    "react/jsx-max-props-per-line": [2, { "maximum": 2, "when": "multiline" }],
    "no-console": [ "error", { allow: ["tron"], }], // for reactotron
    "no-unused-vars": ["error", { "args": "none" }], // used mostly for redux standard functional patterns
    "key-spacing": ["error", { "mode": "minimum"}],
    "arrow-body-style": [0, "as-needed", {
      "requireReturnForObjectLiteral": true
    }],
    "comma-dangle": ["error", "never"],
    "no-underscore-dangle": 0
  },
  globals: {
    // Set each global variable name equal to true to allow the variable to be overwritten
    // or false to disallow overwriting.
    describe: true,
    expect: true,
    test: true,
    beforeEach: true,
    afterEach: true,
    it: false,
    jest: false,
    '__DEV__': false,
    Exception: true
  }
};


