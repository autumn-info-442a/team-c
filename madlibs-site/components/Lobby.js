import React, {Component} from 'react';
import {CopyToClipboard} from 'react-copy-to-clipboard';

class Lobby extends Component {

  constructor(props) {
    super(props);
    this.state = {
      code: "TEST",
      numPlayers: 3,
    }
  }

  render() {
    return(
      <div>
        <div>
          <p>Other players ({this.state.numPlayers}/4)</p>
          <p>Player</p>
          <p>Player</p>
        </div>
        <div>
          <div className="buttonBlock">
            <button>Choice</button>
          </div>
          <div className="buttonBlock">
            <button>Choice</button>
          </div>
          <div className="buttonBlock">
            <button>Choice</button>
          </div>
          <div className="buttonBlock">
            <button>Choice</button>
          </div>
          <div className="buttonBlock">
            <button>Choice</button>
          </div>
        </div>
        <div>
          CODE: {this.state.code}
          <CopyToClipboard text={this.state.code}>
            <button>Copy</button>
          </CopyToClipboard>
        </div>
        <button onClick={this.props.handleStart}>START</button>
      </div>
    );
  }
}

export default Lobby