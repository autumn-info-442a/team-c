import React, {Component} from 'react';

class Prompt extends Component {

  constructor(props) {
    super(props);
    this.state = {
      myTurn: false,
      whoseTurn: "John",
      partOfSpeech: "verb",
    }
  }

  render() {
    return(
      <div>
        {this.state.myTurn ?
          <div>
            Just a moment! {this.state.whoseTurn} is typing in their word.
          </div>
          :
          <div>
            <p>Please enter a {this.state.partOfSpeech}:</p>
            <input type="text" onKeyDown={this.props.handleDone}></input>
          </div>
        }
      </div>
    );
  }
}

export default Prompt